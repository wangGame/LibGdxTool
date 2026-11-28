[CmdletBinding()]
param(
    [Parameter(Mandatory=$true)][string]$Repository,
    [Parameter(Mandatory=$true)][string]$RunId,
    [Parameter(Mandatory=$true)][string]$ArtifactName,
    [Parameter(Mandatory=$true)][string]$OutputDir,
    [Parameter(Mandatory=$true)][string]$GhPath
)
if ($Repository -notmatch '^[A-Za-z0-9_.-]+/[A-Za-z0-9_.-]+$' -or $RunId -notmatch '^\d+$') { throw 'Invalid repository or run ID' }
if ($ArtifactName -notmatch '^TriPeaks-ios-(unsigned|signed)-(\d+)$') { throw 'Invalid artifact name' }
$ipaName = 'TriPeaks-{0}-{1}.ipa' -f $Matches[1],$Matches[2]
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.Net.Http
Add-Type -AssemblyName System.IO.Compression.FileSystem



$metaText = & $ghPath api "repos/$repository/actions/runs/$runId/artifacts"
if ($LASTEXITCODE -ne 0) { throw 'Cannot read artifact metadata' }
$meta = ($metaText -join "`n") | ConvertFrom-Json
$artifact = $meta.artifacts | Where-Object name -eq $ArtifactName | Select-Object -First 1
if (!$artifact -or $artifact.expired) { throw 'Artifact is unavailable' }
$token = & $ghPath auth token --hostname github.com
if ($LASTEXITCODE -ne 0) { throw 'Cannot authenticate download' }
$handler = New-Object Net.Http.HttpClientHandler
$handler.AllowAutoRedirect = $false
$api = New-Object Net.Http.HttpClient($handler)
$request = New-Object Net.Http.HttpRequestMessage([Net.Http.HttpMethod]::Get, "https://api.github.com/repos/$repository/actions/artifacts/$($artifact.id)/zip")
$request.Headers.Authorization = New-Object Net.Http.Headers.AuthenticationHeaderValue('Bearer', ($token -join '').Trim())
$request.Headers.UserAgent.ParseAdd('TriPeaks-Artifact-Downloader')
$redirect = $api.SendAsync($request).GetAwaiter().GetResult()
if ([int]$redirect.StatusCode -ne 302) { throw "Artifact API returned status $([int]$redirect.StatusCode)" }
$location = $redirect.Headers.Location
$token = $null
$request.Dispose(); $redirect.Dispose(); $api.Dispose()
if ($location.Scheme -ne 'https') { throw 'Invalid download scheme' }
$client = New-Object Net.Http.HttpClient
$client.Timeout = [TimeSpan]::FromMinutes(10)
[Net.ServicePointManager]::FindServicePoint($location).ConnectionLimit = 8
$headRequest = New-Object Net.Http.HttpRequestMessage([Net.Http.HttpMethod]::Head, $location)
$head = $client.SendAsync($headRequest).GetAwaiter().GetResult()
$head.EnsureSuccessStatusCode() | Out-Null
$length = [long]$head.Content.Headers.ContentLength
$head.Dispose(); $headRequest.Dispose()
if ($length -le 0) { throw 'Unknown artifact size' }
$tempRoot = Join-Path ([IO.Path]::GetTempPath()) ('tripeaks-parallel-' + [Guid]::NewGuid().ToString('N'))
New-Item -ItemType Directory -Path $tempRoot | Out-Null
$pending = @()
$chunk = [long][Math]::Ceiling($length / 8)
for ($i = 0; $i -lt 8; $i++) {
    $start = $i * $chunk
    $end = [Math]::Min($length - 1, $start + $chunk - 1)
    $rangeRequest = New-Object Net.Http.HttpRequestMessage([Net.Http.HttpMethod]::Get, $location)
    $rangeRequest.Headers.Range = New-Object Net.Http.Headers.RangeHeaderValue($start, $end)
    $pending += [pscustomobject]@{Index=$i; Start=$start; End=$end; Request=$rangeRequest; ResponseTask=$client.SendAsync($rangeRequest,[Net.Http.HttpCompletionOption]::ResponseHeadersRead)}
}
$downloads = @()
foreach ($item in $pending) {
    $response = $item.ResponseTask.GetAwaiter().GetResult()
    if ([int]$response.StatusCode -ne 206 -or $response.Content.Headers.ContentRange.From -ne $item.Start -or $response.Content.Headers.ContentRange.To -ne $item.End) { throw 'Invalid ranged response' }
    $partPath = Join-Path $tempRoot ('part-' + $item.Index)
    $stream = [IO.File]::Create($partPath)
    $downloads += [pscustomobject]@{Path=$partPath; Expected=$item.End-$item.Start+1; Stream=$stream; Response=$response; Request=$item.Request; Task=$response.Content.CopyToAsync($stream)}
}
Write-Host ('Parallel download: {0:N1} MB, 8 connections' -f ($length / 1MB))
while (@($downloads | Where-Object { !$_.Task.IsCompleted }).Count -gt 0) {
    $bytes = ($downloads | ForEach-Object { (Get-Item -LiteralPath $_.Path).Length } | Measure-Object -Sum).Sum
    Write-Host ('Downloaded {0:N1} / {1:N1} MB' -f ($bytes / 1MB), ($length / 1MB))
    Start-Sleep -Seconds 10
}
foreach ($item in $downloads) {
    $item.Task.GetAwaiter().GetResult() | Out-Null
    $item.Stream.Dispose(); $item.Response.Dispose(); $item.Request.Dispose()
    if ((Get-Item -LiteralPath $item.Path).Length -ne $item.Expected) { throw 'Incomplete artifact part' }
}
$zipPath = Join-Path $tempRoot 'artifact.zip'
$merged = [IO.File]::Create($zipPath)
try { foreach ($item in $downloads) { $part = [IO.File]::OpenRead($item.Path); try {$part.CopyTo($merged)} finally {$part.Dispose()} } } finally { $merged.Dispose(); $client.Dispose() }
if ((Get-Item -LiteralPath $zipPath).Length -ne $length) { throw 'Incomplete ZIP' }
if ($artifact.digest -match '^sha256:([a-f0-9]{64})$') {
    $expectedHash = $Matches[1]
    $sha = [Security.Cryptography.SHA256]::Create(); $stream = [IO.File]::OpenRead($zipPath)
    try {$hash = [BitConverter]::ToString($sha.ComputeHash($stream)).Replace('-','').ToLowerInvariant()} finally {$stream.Dispose();$sha.Dispose()}
    if ($hash -ne $expectedHash) { throw 'Artifact ZIP digest mismatch' }
}
$output = [IO.Path]::GetFullPath($OutputDir)
New-Item -ItemType Directory -Path $output -Force | Out-Null
$zip = [IO.Compression.ZipFile]::OpenRead($zipPath)
try {
    foreach ($name in @('build-info.json','SHA256SUMS.txt',$ipaName)) {
        $entry = $zip.GetEntry($name)
        if (!$entry) { throw "Missing artifact file: $name" }
        [IO.Compression.ZipFileExtensions]::ExtractToFile($entry, (Join-Path $output $name), $false)
    }
} finally {$zip.Dispose()}
$resolvedTemp = (Resolve-Path -LiteralPath $tempRoot).Path
$tempParent = [IO.Path]::GetFullPath([IO.Path]::GetTempPath()).TrimEnd([IO.Path]::DirectorySeparatorChar,[IO.Path]::AltDirectorySeparatorChar) + [IO.Path]::DirectorySeparatorChar
if (!$resolvedTemp.StartsWith($tempParent,[StringComparison]::OrdinalIgnoreCase) -or (Split-Path -Leaf $resolvedTemp) -notlike 'tripeaks-parallel-*') {
    throw 'Refusing to clean an unexpected temporary path'
}
try {
    Remove-Item -LiteralPath $resolvedTemp -Recurse -Force
} catch {
    Write-Verbose "Temporary download files retained: $resolvedTemp"
}
Write-Host "Artifact downloaded: $output"
