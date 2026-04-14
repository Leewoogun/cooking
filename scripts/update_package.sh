#!/bin/bash
set -euo pipefail

export LC_ALL=C

# ============================================================
# CMP System - 패키지명 일괄 변경 스크립트
# ============================================================
# 이 Base 프로젝트를 복사하여 새 프로젝트를 만들 때,
# 패키지명과 프로젝트명을 한 번에 변경합니다.
#
# 사용법:
#   ./scripts/update_package.sh [옵션] <새_패키지명> [새_프로젝트명]
#
# 예시:
#   ./scripts/update_package.sh com.example.myapp MyApp
#   ./scripts/update_package.sh --dry-run com.example.myapp MyApp
#
# 옵션:
#   --dry-run          실제 변경 없이 변경 예정 내용만 출력
#   --no-clean-empty   빈 디렉터리 정리 비활성화
# ============================================================

OLD_PACKAGE="com.lwg.base"
OLD_PROJECT_NAME="CmpSystem"

# === 프로젝트 루트 감지 ===
if [[ -z "${PROJECT_DIR:-}" ]]; then
  if git_root=$(git rev-parse --show-toplevel 2>/dev/null); then
    PROJECT_DIR="$git_root"
  else
    SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
    PARENT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
    if [[ -f "$PARENT_DIR/settings.gradle.kts" ]]; then
      PROJECT_DIR="$PARENT_DIR"
    else
      PROJECT_DIR="$(pwd)"
    fi
  fi
fi
echo "📂 프로젝트 경로: $PROJECT_DIR"

# === 옵션 파싱 ===
DRY_RUN=0
CLEAN_EMPTY=1
while [[ $# -gt 0 && "${1:-}" == --* ]]; do
  case "$1" in
    --dry-run) DRY_RUN=1 ;;
    --clean-empty) CLEAN_EMPTY=1 ;;
    --no-clean-empty) CLEAN_EMPTY=0 ;;
    *) echo "❌ 알 수 없는 옵션: $1"; exit 1 ;;
  esac
  shift
done

# === 새 패키지명 입력 ===
if [ -n "${1-}" ]; then
  NEW_PACKAGE="$1"
else
  read -rp "새 패키지명 입력 (예: com.example.myapp): " NEW_PACKAGE
fi
NEW_PACKAGE="$(echo -n "${NEW_PACKAGE:-}" | tr -d '[:space:]')"

if [ -z "$NEW_PACKAGE" ]; then
  echo "❌ 새 패키지명이 비었습니다."
  exit 1
fi
if ! [[ "$NEW_PACKAGE" =~ ^[A-Za-z_][A-Za-z0-9_]*(\.[A-Za-z_][A-Za-z0-9_]*)*$ ]]; then
  echo "❌ 유효하지 않은 패키지명: $NEW_PACKAGE"
  exit 1
fi

# === 새 프로젝트명 입력 ===
if [ -n "${2-}" ]; then
  NEW_PROJECT_NAME="$2"
else
  read -rp "새 프로젝트명 입력 (예: MyApp, 빈 값이면 '$OLD_PROJECT_NAME' 유지): " NEW_PROJECT_NAME
  NEW_PROJECT_NAME="${NEW_PROJECT_NAME:-$OLD_PROJECT_NAME}"
fi

# === 경로 변환 ===
OLD_PACKAGE_PATH="$(tr '.' '/' <<<"$OLD_PACKAGE")"
NEW_PACKAGE_PATH="$(tr '.' '/' <<<"$NEW_PACKAGE")"

# === 플러그인 관련 변환값 계산 ===
# OLD_PACKAGE의 마지막 세그먼트 (base) → 플러그인 ID prefix
OLD_PKG_LAST="${OLD_PACKAGE##*.}"
NEW_PKG_LAST="${NEW_PACKAGE##*.}"

# camelCase alias prefix: base → base, myapp → myapp
OLD_ALIAS_PREFIX="$OLD_PKG_LAST"
NEW_ALIAS_PREFIX="$NEW_PKG_LAST"

# PascalCase class prefix: Base → MyApp
OLD_CLASS_PREFIX="$(echo "${OLD_PKG_LAST:0:1}" | tr '[:lower:]' '[:upper:]')${OLD_PKG_LAST:1}"
NEW_CLASS_PREFIX="$(echo "${NEW_PKG_LAST:0:1}" | tr '[:lower:]' '[:upper:]')${NEW_PKG_LAST:1}"

# Compose Resources generated import prefix (rootProject.name 기반, lowercase)
OLD_RES_PREFIX="$(echo "$OLD_PROJECT_NAME" | tr '[:upper:]' '[:lower:]')"
NEW_RES_PREFIX="$(echo "$NEW_PROJECT_NAME" | tr '[:upper:]' '[:lower:]')"

echo ""
echo "======================================="
echo "  패키지 변경 요약"
echo "======================================="
echo "  패키지:       $OLD_PACKAGE → $NEW_PACKAGE"
echo "  프로젝트명:    $OLD_PROJECT_NAME → $NEW_PROJECT_NAME"
echo "  플러그인 ID:   $OLD_PKG_LAST.xxx → $NEW_PKG_LAST.xxx"
echo "  플러그인 alias: ${OLD_ALIAS_PREFIX}Xxx → ${NEW_ALIAS_PREFIX}Xxx"
echo "  클래스 prefix:  ${OLD_CLASS_PREFIX}Xxx → ${NEW_CLASS_PREFIX}Xxx"
echo "  리소스 import:  ${OLD_RES_PREFIX}.xxx → ${NEW_RES_PREFIX}.xxx"
echo "  디렉터리:      $OLD_PACKAGE_PATH → $NEW_PACKAGE_PATH"
if ((DRY_RUN)); then
  echo "  모드:          DRY-RUN (실제 변경 없음)"
fi
echo "======================================="
echo ""

# === 헬퍼 함수 ===
run() {
  if ((DRY_RUN)); then
    printf "[DRY-RUN]"
    printf " %q" "$@"
    echo
  else
    "$@"
  fi
}

sed_replace_file() {
  local file="$1"
  if ((DRY_RUN)); then
    echo "[DRY-RUN] sed replace in $file"
    return
  fi
  # macOS sed -i requires '' argument
  if sed --version >/dev/null 2>&1; then
    # 공통 sed 표현식 배열
    local sed_exprs=(
      -e "s|$OLD_PACKAGE|$NEW_PACKAGE|g"
      -e "s|${OLD_ALIAS_PREFIX}KotlinMultiplatformPure|${NEW_ALIAS_PREFIX}KotlinMultiplatformPure|g"
      -e "s|${OLD_ALIAS_PREFIX}KotlinMultiplatform|${NEW_ALIAS_PREFIX}KotlinMultiplatform|g"
      -e "s|${OLD_ALIAS_PREFIX}ComposeMultiplatform|${NEW_ALIAS_PREFIX}ComposeMultiplatform|g"
      -e "s|${OLD_ALIAS_PREFIX}Feature|${NEW_ALIAS_PREFIX}Feature|g"
      -e "s|${OLD_ALIAS_PREFIX}KspKoin|${NEW_ALIAS_PREFIX}KspKoin|g"
      -e "s|${OLD_ALIAS_PREFIX}KmpIos|${NEW_ALIAS_PREFIX}KmpIos|g"
      -e "s|${OLD_ALIAS_PREFIX}KmpAndroid|${NEW_ALIAS_PREFIX}KmpAndroid|g"
      -e "s|${OLD_ALIAS_PREFIX}Kmp|${NEW_ALIAS_PREFIX}Kmp|g"
      -e "s|${OLD_ALIAS_PREFIX}VerifyDetekt|${NEW_ALIAS_PREFIX}VerifyDetekt|g"
      -e "s|\"${OLD_PKG_LAST}\.|\"${NEW_PKG_LAST}.|g"
      -e "s|${OLD_CLASS_PREFIX}FeaturePlugin|${NEW_CLASS_PREFIX}FeaturePlugin|g"
      -e "s|${OLD_CLASS_PREFIX}BottomBar|${NEW_CLASS_PREFIX}BottomBar|g"
      -e "s|${OLD_CLASS_PREFIX}Application|${NEW_CLASS_PREFIX}Application|g"
      -e "s|${OLD_CLASS_PREFIX}TypoGraphy|${NEW_CLASS_PREFIX}TypoGraphy|g"
      -e "s|AppTheme|${NEW_CLASS_PREFIX}Theme|g"
      -e "s|AppScaffold|${NEW_CLASS_PREFIX}Scaffold|g"
      -e "s|AppLabel|${NEW_CLASS_PREFIX}Label|g"
      -e "s|AppTypoGraphy|${NEW_CLASS_PREFIX}TypoGraphy|g"
      -e "s|AppColorScheme|${NEW_CLASS_PREFIX}ColorScheme|g"
      -e "s|AppButton|${NEW_CLASS_PREFIX}Button|g"
      -e "s|$OLD_PROJECT_NAME|$NEW_PROJECT_NAME|g"
      -e "s|${OLD_RES_PREFIX}\.\(core\|feature\)|${NEW_RES_PREFIX}.\1|g"
    )
    # GNU vs BSD sed
    if sed --version >/dev/null 2>&1; then
      sed -i "${sed_exprs[@]}" "$file"
    else
      sed -i '' "${sed_exprs[@]}" "$file"
    fi
  fi
}

# === 0) 안전장치: git 워킹트리 상태 안내 ===
if git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
  git diff --quiet 2>/dev/null || echo "⚠️  경고: 변경사항이 있습니다. 커밋/스태시 후 진행을 권장합니다."
fi

# === 1) 문자열 치환 (소스코드 + 빌드 파일) ===
echo "📝 [1/5] 파일 내 패키지/플러그인 문자열 치환..."
find "$PROJECT_DIR" -type f \( \
  -name "*.kt" -o -name "*.kts" -o -name "*.java" -o \
  -name "*.xml" -o -name "*.gradle" -o -name "*.properties" -o \
  -name "*.toml" -o -name "*.sh" \
\) \
  -not -path "*/build/*" \
  -not -path "*/.gradle/*" \
  -not -path "*/.git/*" \
  -not -path "*/.idea/*" \
  -not -path "*/update_package.sh" \
  -print0 | while IFS= read -r -d '' file; do
    sed_replace_file "$file"
  done

# === 2) iOS 설정 파일 치환 ===
echo "🍎 [2/5] iOS 설정 파일 치환..."
XCCONFIG="$PROJECT_DIR/iosApp/Configuration/Config.xcconfig"
if [[ -f "$XCCONFIG" ]]; then
  if ((DRY_RUN)); then
    echo "[DRY-RUN] sed replace in $XCCONFIG"
  else
    if sed --version >/dev/null 2>&1; then
      sed -i \
        -e "s|$OLD_PACKAGE|$NEW_PACKAGE|g" \
        -e "s|$OLD_PROJECT_NAME|$NEW_PROJECT_NAME|g" \
        "$XCCONFIG"
    else
      sed -i '' \
        -e "s|$OLD_PACKAGE|$NEW_PACKAGE|g" \
        -e "s|$OLD_PROJECT_NAME|$NEW_PROJECT_NAME|g" \
        "$XCCONFIG"
    fi
    echo "  ✓ Config.xcconfig 업데이트 완료"
  fi
fi

# === 3) 디렉터리 이동 (com/lwg/base → new path) ===
echo "📁 [3/5] 소스 디렉터리 이동..."
find "$PROJECT_DIR" -type d -path "*/$OLD_PACKAGE_PATH" \
  -not -path "*/build/*" \
  -not -path "*/.gradle/*" \
  -not -path "*/.git/*" \
  -not -path "*/.idea/*" \
  -not -path "*/update_package.sh" \
  | sort -r | while IFS= read -r old_dir; do
    new_dir="${old_dir%"$OLD_PACKAGE_PATH"}$NEW_PACKAGE_PATH"
    if [[ "$old_dir" == "$new_dir" ]]; then
      continue
    fi
    echo "  이동: $old_dir → $new_dir"
    if ((DRY_RUN)); then
      echo "  [DRY-RUN] mkdir -p $new_dir && mv contents"
    else
      mkdir -p "$new_dir"
      # Move all contents
      find "$old_dir" -maxdepth 1 -mindepth 1 -exec mv {} "$new_dir/" \;
    fi
  done

# === 4) 파일명 변경 (XxxFeaturePlugin.kt, XxxApplication.kt 등) ===
echo "📄 [4/5] 클래스 파일명 변경..."
find "$PROJECT_DIR" -type f -name "${OLD_CLASS_PREFIX}*.kt" \
  -not -path "*/build/*" \
  -not -path "*/.gradle/*" \
  -not -path "*/.git/*" \
  -not -path "*/.idea/*" \
  | while IFS= read -r old_file; do
    filename="$(basename "$old_file")"
    new_filename="${filename/$OLD_CLASS_PREFIX/$NEW_CLASS_PREFIX}"
    if [[ "$filename" != "$new_filename" ]]; then
      new_file="$(dirname "$old_file")/$new_filename"
      echo "  이름변경: $filename → $new_filename"
      if ((DRY_RUN)); then
        echo "  [DRY-RUN] mv $old_file $new_file"
      else
        mv "$old_file" "$new_file"
      fi
    fi
  done

# === 5) 잔여 빈 디렉터리 정리 ===
if ((CLEAN_EMPTY)); then
  echo "🧹 [5/5] 빈 디렉터리 정리..."
  if ((DRY_RUN)); then
    echo "[DRY-RUN] find and delete empty dirs"
  else
    find "$PROJECT_DIR" -type d -empty \
      -not -path "*/.git/*" -not -path "*/.idea/*" -not -path "*/.gradle/*" \
      -delete 2>/dev/null || true
  fi
else
  echo "⏭️  [5/5] 빈 디렉터리 정리 생략 (--no-clean-empty)"
fi

echo ""
echo "======================================="
echo "✅ 패키지 변경 완료!"
echo "======================================="
echo ""
echo "변경 내용:"
echo "  📦 패키지:    $OLD_PACKAGE → $NEW_PACKAGE"
echo "  📱 프로젝트:  $OLD_PROJECT_NAME → $NEW_PROJECT_NAME"
echo ""
echo "다음 단계:"
echo "  1. Android Studio에서 'Invalidate Caches / Restart' 실행"
echo "  2. Gradle Sync 실행"
echo "  3. 빌드 확인: ./gradlew :composeApp:compileCommonMainKotlinMetadata"
echo ""
