#!/usr/bin/env bash
set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BUILD_DIR="$PROJECT_ROOT/build/classes"

rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"
mapfile -d '' MAIN_SOURCES < <(find "$PROJECT_ROOT/src/main/java" -name '*.java' -print0)
mapfile -d '' TEST_SOURCES < <(find "$PROJECT_ROOT/src/test/java" -name '*.java' -print0)

if command -v javac >/dev/null 2>&1; then
    COMPILER=(javac)
elif java --list-modules | grep -q '^jdk.compiler@'; then
    COMPILER=(java -m jdk.compiler/com.sun.tools.javac.Main)
else
    echo "A JDK 17 compiler is required (javac or the jdk.compiler module)." >&2
    exit 1
fi

"${COMPILER[@]}" --release 17 -Xlint:all -Werror -d "$BUILD_DIR" "${MAIN_SOURCES[@]}" "${TEST_SOURCES[@]}"
java -ea -cp "$BUILD_DIR" edu.aitu.facade.hometheater.verification.FacadeBehaviorChecks
java -cp "$BUILD_DIR" edu.aitu.facade.hometheater.client.HomeTheaterDemo
