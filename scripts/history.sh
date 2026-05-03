#!/usr/bin/env bash

GIT_ROOT="$1"
WORK_DIR="$2"
REPORT_FILE="$3"
LOG_FILE="$4"

REPO_COPY="$WORK_DIR/repo"

rm -rf "$WORK_DIR"
mkdir -p "$WORK_DIR"
mkdir -p "$(dirname "$REPORT_FILE")"
mkdir -p "$(dirname "$LOG_FILE")"

echo "History target started" > "$LOG_FILE"
echo "Git root: $GIT_ROOT" >> "$LOG_FILE"

if [ ! -d "$GIT_ROOT/.git" ]; then
    echo "ERROR: $GIT_ROOT is not a Git repository" | tee -a "$LOG_FILE"
    exit 1
fi

git clone "$GIT_ROOT" "$REPO_COPY" >> "$LOG_FILE" 2>&1
cd "$REPO_COPY" || exit 1

BAD_COMMIT=""

for COMMIT in $(git rev-list --first-parent HEAD); do
    echo "Trying commit: $COMMIT" >> "$LOG_FILE"

    git checkout --quiet "$COMMIT" >> "$LOG_FILE" 2>&1

    if ant -q compile >> "$LOG_FILE" 2>&1; then
        echo "Found working commit: $COMMIT" >> "$LOG_FILE"

        if [ -z "$BAD_COMMIT" ]; then
            echo "Current revision compiles. No rollback needed." > "$REPORT_FILE"
        else
            {
                echo "Last working revision: $COMMIT"
                echo "Broken revision immediately after it: $BAD_COMMIT"
                echo
                echo "Changed files:"
                git diff --name-only "$COMMIT" "$BAD_COMMIT"
                echo
                echo "Diff:"
                git diff "$COMMIT" "$BAD_COMMIT"
            } > "$REPORT_FILE"
        fi

        exit 0
    fi

    echo "Commit does not compile: $COMMIT" >> "$LOG_FILE"
    BAD_COMMIT="$COMMIT"
done

echo "No compilable revision was found." | tee "$REPORT_FILE" >> "$LOG_FILE"
exit 1