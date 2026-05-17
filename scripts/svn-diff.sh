#!/usr/bin/env bash

set -e

SOURCE_ROOT="$1"
PROTECTED_CLASSES="$2"
COMMIT_PATHS="$3"
COMMIT_MESSAGE="$4"
REPORT_FILE="$5"

mkdir -p "$(dirname "$REPORT_FILE")"

echo "--- SVN STATUS ---" > "$REPORT_FILE"
svn status >> "$REPORT_FILE"
echo >> "$REPORT_FILE"

STATUS="$(svn status)"

if [ -z "$STATUS" ]; then
    echo "Working copy is clean. Nothing to commit." >> "$REPORT_FILE"
    exit 0
fi

echo "--- CHECK PROTECTED CLASSES ---" >> "$REPORT_FILE"

for CLASS_NAME in $PROTECTED_CLASSES; do
    CLASS_PATH="$SOURCE_ROOT/${CLASS_NAME//./\/}.java"

    echo "Checking protected class: $CLASS_PATH" >> "$REPORT_FILE"

    if echo "$STATUS" | awk '{print $NF}' | grep -qx "$CLASS_PATH"; then
        echo "Protected class was changed: $CLASS_PATH" >> "$REPORT_FILE"
        echo "SVN commit skipped." >> "$REPORT_FILE"
        exit 0
    fi
done

echo "No protected classes were changed." >> "$REPORT_FILE"
echo >> "$REPORT_FILE"

echo "--- SVN DIFF ---" >> "$REPORT_FILE"
svn diff >> "$REPORT_FILE"

echo >> "$REPORT_FILE"
echo "--- SVN ADD ---" >> "$REPORT_FILE"
svn add --force $COMMIT_PATHS >> "$REPORT_FILE" 2>&1 || true

echo >> "$REPORT_FILE"
echo "--- SVN STATUS AFTER ADD ---" >> "$REPORT_FILE"
svn status $COMMIT_PATHS >> "$REPORT_FILE"

if [ -z "$(svn status $COMMIT_PATHS)" ]; then
    echo "Nothing to commit after svn add." >> "$REPORT_FILE"
    exit 0
fi

echo >> "$REPORT_FILE"
echo "--- SVN COMMIT ---" >> "$REPORT_FILE"
svn commit $COMMIT_PATHS -m "$COMMIT_MESSAGE" >> "$REPORT_FILE" 2>&1

echo "SVN commit completed." >> "$REPORT_FILE"