"""Convert all game OGG audio, retaining paths. Requires ffmpeg or imageio-ffmpeg."""
from pathlib import Path
import shutil
import subprocess
root = Path(__file__).resolve().parents[1]
ffmpeg = shutil.which('ffmpeg')
if not ffmpeg:
    try:
        import imageio_ffmpeg
        ffmpeg = imageio_ffmpeg.get_ffmpeg_exe()
    except ImportError:
        raise SystemExit('Install ffmpeg, or run: python -m pip install imageio-ffmpeg')
for source in sorted((root / 'assets').rglob('*.ogg')):
    target = root / 'ios-moe/media' / source.relative_to(root / 'assets').with_suffix('.mp3')
    target.parent.mkdir(parents=True, exist_ok=True)
    subprocess.run([ffmpeg, '-nostdin', '-y', '-loglevel', 'error', '-i', str(source),
                    '-codec:a', 'libmp3lame', '-q:a', '2', str(target)], check=True)
    print(target.relative_to(root))
