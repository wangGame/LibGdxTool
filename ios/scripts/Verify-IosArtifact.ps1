[CmdletBinding()]
param(
    [Parameter(Mandatory = $true)][string]$ArtifactDir,
    [Parameter(Mandatory = $true)][string]$RunId,
    [Parameter(Mandatory = $true)][string]$Commit,
    [Parameter(Mandatory = $true)][string]$Repository
)

$ErrorActionPreference = 'Stop'
$directory = (Resolve-Path -LiteralPath $ArtifactDir).Path
$infoPath = Join-Path $directory 'build-info.json'
$hashPath = Join-Path $directory 'SHA256SUMS.txt'
if (!(Test-Path -LiteralPath $infoPath) -or !(Test-Path -LiteralPath $hashPath)) {
    throw 'Artifact is missing build-info.json or SHA256SUMS.txt. Publish the updated workflow first.'
}
$info = Get-Content -LiteralPath $infoPath -Raw | ConvertFrom-Json
if ([string]$info.run_id -ne $RunId -or [string]$info.commit -ne $Commit -or [string]$info.repository -ne $Repository) {
    throw 'Build metadata does not match the selected GitHub run, repository or commit.'
}
if ($info.architecture -ne 'arm64' -or [string]$info.build -ne [string]$info.run_number) {
    throw 'Invalid architecture or build number in build metadata.'
}
foreach ($check in @('bundle_id', 'build_number', 'arm64', 'game_resources', 'launch_storyboard', 'privacy_manifest')) {
    if ($check -notin $info.checks) { throw "Cloud validation is missing: $check" }
}
if ($info.signing -notin @('unsigned', 'signed')) { throw 'Invalid signing mode.' }
if ($info.signing -eq 'signed' -and 'codesign' -notin $info.checks) { throw 'Cloud code signing verification is missing.' }

$ipas = @(Get-ChildItem -LiteralPath $directory -Filter '*.ipa' -File)
if ($ipas.Count -ne 1) { throw "Expected one IPA, found $($ipas.Count)." }
$ipa = $ipas[0]
$hashes = @(Get-Content -LiteralPath $hashPath | Where-Object { $_.Trim() })
if ($hashes.Count -ne 1 -or $hashes[0] -notmatch '^([a-fA-F0-9]{64})\s+\*?([^/\\]+\.ipa)$') {
    throw 'Expected one SHA-256 entry containing a plain IPA filename.'
}
$expectedHash = $Matches[1]
if ($Matches[2] -cne $ipa.Name) { throw 'Checksum filename does not match the downloaded IPA.' }
$hashStream = [IO.File]::OpenRead($ipa.FullName)
$hashAlgorithm = [Security.Cryptography.SHA256]::Create()
try {
    $actualHash = [BitConverter]::ToString($hashAlgorithm.ComputeHash($hashStream)).Replace('-', '')
} finally { $hashStream.Dispose(); $hashAlgorithm.Dispose() }
if ($actualHash -ne $expectedHash) { throw 'SHA-256 mismatch: the downloaded IPA is incomplete or has changed.' }

Add-Type -AssemblyName System.IO.Compression.FileSystem
$archive = [IO.Compression.ZipFile]::OpenRead($ipa.FullName)
try {
    $entries = @{}
    foreach ($entry in $archive.Entries) {
        $name = $entry.FullName -replace '^(\./)+', ''
        if ($entries.ContainsKey($name)) { throw "Duplicate IPA entry: $name" }
        $entries[$name] = $entry
    }
    $plists = @($entries.Keys | Where-Object { $_ -match '^Payload/[^/]+\.app/Info\.plist$' })
    if ($plists.Count -ne 1) { throw 'Expected one app Info.plist under Payload/.' }
    $prefix = $plists[0].Substring(0, $plists[0].Length - 'Info.plist'.Length)
    if ([string]$info.executable -notmatch '^[A-Za-z0-9_.-]+$') { throw 'Invalid executable name.' }
    foreach ($name in @('Info.plist', [string]$info.executable, 'images/dark_title.png', 'PrivacyInfo.xcprivacy')) {
        if (!$entries.ContainsKey($prefix + $name) -or $entries[$prefix + $name].Length -eq 0) {
            throw "Missing or empty IPA file: $name"
        }
    }
    $launch = @($entries.Keys | Where-Object { $_.StartsWith($prefix) -and $_ -match '/LaunchScreen\.storyboardc/.+' })
    if ($launch.Count -eq 0) { throw 'Compiled launch storyboard is missing.' }
    if ($info.signing -eq 'signed' -and !$entries.ContainsKey($prefix + 'embedded.mobileprovision')) {
        throw 'Signed IPA has no provisioning profile.'
    }
    $stream = $entries[$prefix + $info.executable].Open()
    $reader = New-Object IO.BinaryReader($stream)
    try {
        # The workflow builds a thin little-endian ARM64 Mach-O executable.
        if ($reader.ReadUInt32() -ne 4277009103 -or $reader.ReadUInt32() -ne 16777228) {
            throw 'IPA executable is not a 64-bit ARM64 Mach-O binary.'
        }
    } finally { $reader.Dispose() }
} finally { $archive.Dispose() }

$report = [ordered]@{
    result = 'passed'; ipa = $ipa.Name; sha256 = $actualHash.ToLowerInvariant()
    repository = $Repository; commit = $Commit; run_id = $RunId; signing = $info.signing
    checked_at_utc = [DateTime]::UtcNow.ToString('o')
    checks = @('cloud_validation', 'run_and_commit', 'sha256', 'ipa_structure', 'arm64_macho')
}
$utf8 = New-Object Text.UTF8Encoding($false)
[IO.File]::WriteAllText((Join-Path $directory 'local-check.json'), ($report | ConvertTo-Json -Depth 4), $utf8)
Write-Host "Verified IPA: $($ipa.FullName)"
Write-Host "Commit: $Commit; signing: $($info.signing); SHA-256: $actualHash"
Write-Output $ipa.FullName
