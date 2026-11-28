#!/bin/bash
set -euo pipefail

export MOE_BUILD_SOURCE_SET=main
export MOE_BUILD_MODE
MOE_BUILD_MODE="$(echo "$CONFIGURATION" | tr '[:upper:]' '[:lower:]')"
export JAVA_HOME="${JAVA_HOME:-$(/usr/libexec/java_home -v 17)}"
cd "$MOE_PROJECT_DIR/.."
if [[ -z "${MOE_GRADLE_EXTERNAL_BUILD:-}" ]]; then
    bash gradlew -PiosMoeOnly=true :ios-moe:moeXcodeInternal --console=plain
fi

resources="$TARGET_BUILD_DIR/$UNLOCALIZED_RESOURCES_FOLDER_PATH"
frameworks="$TARGET_BUILD_DIR/$FRAMEWORKS_FOLDER_PATH"
mkdir -p "$resources" "$frameworks"
cp "$MOE_PROJECT_BUILD_DIR/moe/main/resources/$MOE_BUILD_MODE/application.jar" "$resources/"
cp "$MOE_PROJECT_BUILD_DIR/moe/main/startup-provider/$MOE_BUILD_MODE/preregister.txt" "$resources/"
rsync -a --delete "$MOE_PROJECT_BUILD_DIR/assets/" "$resources/assets/"
rsync -a --delete --exclude Headers --exclude PrivateHeaders --exclude Modules \
    "$MOE_FRAMEWORK_PATH/MOE.framework/" "$frameworks/MOE.framework/"
if [[ "$PLATFORM_NAME" == iphonesimulator && "$ARCHS" != *" "* ]]; then
    xcrun lipo -thin "$ARCHS" "$MOE_FRAMEWORK_PATH/MOE.framework/MOE" -output "$frameworks/MOE.framework/MOE"
fi
if [[ -n "${EXPANDED_CODE_SIGN_IDENTITY:-}" && "${CODE_SIGNING_ALLOWED:-YES}" != NO ]]; then
    /usr/bin/codesign --force --sign "$EXPANDED_CODE_SIGN_IDENTITY" "$frameworks/MOE.framework"
fi
