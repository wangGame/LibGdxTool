# GWT web module

This module is isolated from the existing Android and desktop launchers.

Build an optimized multi-file web distribution with:

```shell
gradlew html:dist
```

The output is written to `html/build/dist`. This is the browser-compilation
stage; converting that distribution into one self-contained playable-ad HTML
is intentionally a separate packaging step.
