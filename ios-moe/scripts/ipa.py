"""Package on macOS; verify downloaded unsigned MOE IPA on any Python 3 host."""
import argparse
import hashlib
import json
import os
from pathlib import Path
import plistlib
import re
import shutil
import struct
import subprocess
import tempfile
import zipfile


def require(condition, message):
    if not condition:
        raise ValueError(message)


def sha256(path):
    with path.open('rb') as stream:
        return hashlib.file_digest(stream, 'sha256').hexdigest()


def package(args):
    app = args.app.resolve()
    assets = args.assets.resolve()
    require(app.is_dir() and assets.is_dir(), 'App or prepared assets directory missing')
    info = plistlib.loads((app / 'Info.plist').read_bytes())
    executable = info['CFBundleExecutable']
    require(info['CFBundleIdentifier'] == os.environ['IOS_BUNDLE_ID'], 'Bundle ID mismatch')
    require(info['CFBundleVersion'] == os.environ['GITHUB_RUN_NUMBER'], 'Build number mismatch')
    require(info['CFBundleShortVersionString'] == os.environ['IOS_APP_VERSION'], 'Version mismatch')
    require(info['MOE.Main.Class'] == 'com.tony.dominoes.iosmoe.Main', 'Wrong Java entry point')
    archs = subprocess.check_output(['xcrun', 'lipo', '-archs', str(app / executable)], text=True).strip()
    require(archs == 'arm64', f'Expected thin ARM64 executable, got {archs}')
    for framework in ('MOE', 'gdx', 'ObjectAL', 'MetalANGLE'):
        binary = app / f'Frameworks/{framework}.framework/{framework}'
        require(binary.is_file(), f'Missing native framework: {framework}')
        native_archs = subprocess.check_output(['xcrun', 'lipo', '-archs', str(binary)], text=True).split()
        require('arm64' in native_archs, f'Framework has no ARM64 slice: {framework}')
    for source in assets.rglob('*'):
        if source.is_file():
            target = app / 'assets' / source.relative_to(assets)
            require(target.is_file() and sha256(target) == sha256(source), f'Missing or changed asset: {source}')
    require(any(app.rglob(info['UILaunchStoryboardName'] + '.storyboardc')), 'Missing launch storyboard')
    for name in ('application.jar', 'preregister.txt'):
        require((app / name).is_file() and (app / name).stat().st_size > 0, f'Missing MOE runtime resource: {name}')
    output = args.output.resolve()
    output.mkdir(parents=True, exist_ok=True)
    ipa_name = f"Dominoes-unsigned-{os.environ['GITHUB_RUN_NUMBER']}.ipa"
    # ditto preserves executable modes, bundle layout and framework symlinks.
    with tempfile.TemporaryDirectory() as temp:
        payload = Path(temp) / 'Payload'
        payload.mkdir()
        shutil.copytree(app, payload / app.name, symlinks=True)
        subprocess.run(['ditto', '-c', '-k', '--sequesterRsrc', '--keepParent', str(payload), str(output / ipa_name)], check=True)
    metadata = {
        'repository': os.environ['GITHUB_REPOSITORY'], 'commit': os.environ['GITHUB_SHA'],
        'run_id': os.environ['GITHUB_RUN_ID'], 'run_number': os.environ['GITHUB_RUN_NUMBER'],
        'signing': 'unsigned', 'bundle_id': info['CFBundleIdentifier'],
        'version': info['CFBundleShortVersionString'], 'build': info['CFBundleVersion'],
        'architecture': archs, 'executable': executable,
        'checks': ['bundle_id', 'build_number', 'arm64', 'game_resources', 'launch_storyboard', 'moe_runtime', 'native_frameworks'],
        'files': {str(p.relative_to(app)).replace('\\', '/'): sha256(p) for p in app.rglob('*') if p.is_file()},
    }
    (output / 'build-info.json').write_text(json.dumps(metadata, indent=2) + '\n', encoding='utf-8')
    (output / 'SHA256SUMS.txt').write_text(sha256(output / ipa_name) + '  ' + ipa_name + '\n', encoding='utf-8')
    verify(argparse.Namespace(directory=output, repository=metadata['repository'], commit=metadata['commit'], run_id=metadata['run_id']))


def verify(args):
    directory = args.directory.resolve()
    metadata = json.loads((directory / 'build-info.json').read_text(encoding='utf-8'))
    for key, expected in [('repository', args.repository), ('commit', args.commit), ('run_id', args.run_id)]:
        require(str(metadata[key]) == str(expected), f'Artifact {key} does not match selected run')
    require(metadata['signing'] == 'unsigned' and metadata['architecture'] == 'arm64', 'Unexpected signing or architecture')
    require(str(metadata['build']) == str(metadata['run_number']), 'Build number mismatch')
    for check in ('bundle_id', 'build_number', 'arm64', 'game_resources', 'launch_storyboard', 'moe_runtime', 'native_frameworks'):
        require(check in metadata['checks'], f'Missing cloud check: {check}')
    checksum = (directory / 'SHA256SUMS.txt').read_text(encoding='utf-8').strip()
    match = re.fullmatch(r'([a-fA-F0-9]{64})\s+([^/\\]+\.ipa)', checksum)
    require(match is not None, 'Invalid IPA checksum entry')
    ipas = list(directory.glob('*.ipa'))
    require(len(ipas) == 1 and ipas[0].name == match[2], 'Expected exactly one matching IPA')
    require(sha256(ipas[0]) == match[1].lower(), 'IPA SHA256 mismatch')
    with zipfile.ZipFile(ipas[0]) as archive:
        names = archive.namelist()
        require(len(names) == len(set(names)), 'Duplicate IPA entries')
        plists = [n for n in names if re.fullmatch(r'Payload/[^/]+\.app/Info\.plist', n)]
        require(len(plists) == 1, 'Expected one app in Payload')
        prefix = plists[0][:-len('Info.plist')]
        info = plistlib.loads(archive.read(plists[0]))
        for key, expected in [('CFBundleIdentifier', metadata['bundle_id']), ('CFBundleVersion', metadata['build']),
                              ('CFBundleShortVersionString', metadata['version']), ('CFBundleExecutable', metadata['executable']),
                              ('MOE.Main.Class', 'com.tony.dominoes.iosmoe.Main')]:
            require(str(info[key]) == str(expected), f'IPA plist mismatch: {key}')
        require(re.fullmatch(r'[A-Za-z0-9_.-]+', metadata['executable']), 'Invalid executable name')
        header = archive.read(prefix + metadata['executable'])[:8]
        require(header == struct.pack('<II', 0xfeedfacf, 0x0100000c), 'Executable is not thin ARM64 Mach-O')
        files = metadata['files']
        require(len(files) > 0, 'Empty file manifest')
        for required in ('Info.plist', metadata['executable'], 'application.jar', 'preregister.txt',
                         'Frameworks/MOE.framework/MOE', 'Frameworks/gdx.framework/gdx',
                         'Frameworks/ObjectAL.framework/ObjectAL', 'Frameworks/MetalANGLE.framework/MetalANGLE'):
            require(required in files, f'Manifest missing core file: {required}')
        require(any(n.startswith('assets/') for n in files), 'No game assets in manifest')
        require(any('.storyboardc/' in n for n in files), 'No compiled launch screen in manifest')
        for relative, expected in files.items():
            require(not relative.startswith('/') and '..' not in Path(relative).parts, 'Unsafe manifest path')
            require(hashlib.sha256(archive.read(prefix + relative)).hexdigest() == expected, f'IPA file mismatch: {relative}')
    (directory / 'local-check.json').write_text(json.dumps({'result': 'passed', 'ipa': ipas[0].name,
        'commit': args.commit, 'run_id': args.run_id, 'sha256': match[1].lower()}, indent=2) + '\n', encoding='utf-8')
    print(f'Verified IPA: {ipas[0]}')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    commands = parser.add_subparsers(dest='command', required=True)
    create = commands.add_parser('package')
    create.add_argument('--app', type=Path, required=True)
    create.add_argument('--assets', type=Path, required=True)
    create.add_argument('--output', type=Path, required=True)
    create.set_defaults(func=package)
    check = commands.add_parser('verify')
    check.add_argument('--directory', type=Path, required=True)
    for option in ('repository', 'commit', 'run-id'):
        check.add_argument('--' + option, required=True)
    check.set_defaults(func=verify)
    arguments = parser.parse_args()
    arguments.func(arguments)
