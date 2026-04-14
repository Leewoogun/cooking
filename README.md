# CMP System

Kotlin Multiplatform + Compose Multiplatform 기반 프로젝트 템플릿.

이 레포지토리를 복사하여 새 프로젝트를 빠르게 시작할 수 있습니다.

---

## 새 프로젝트 만들기

### 1. 레포지토리 복사

```bash
# GitHub에서 "Use this template"으로 새 레포 생성 후 clone
git clone https://github.com/<your-org>/<new-project>.git
cd <new-project>
```

또는 직접 복사:

```bash
cp -R CmpSystem/ MyNewProject/
cd MyNewProject
rm -rf .git
git init
```

### 2. 패키지명 & 프로젝트명 변경

```bash
./scripts/update_package.sh <패키지명> <프로젝트명>
```

**예시:**

```bash
# 음식 배달 앱
./scripts/update_package.sh com.example.fooddelivery FoodDelivery

**변경 전 미리보기 (dry-run):**

```bash
./scripts/update_package.sh --dry-run com.example.myapp MyApp
```

이 스크립트가 자동으로 처리하는 항목:

| 항목 | 변경 예시 |
|---|---|
| 패키지명 | `com.lwg.base` -> `com.example.myapp` |
| 프로젝트명 (settings.gradle.kts) | `CmpSystem` -> `MyApp` |
| build-logic 플러그인 ID | `base.kotlin.multiplatform` -> `myapp.kotlin.multiplatform` |
| version catalog 플러그인 alias | `baseKotlinMultiplatform` -> `myappKotlinMultiplatform` |
| 디자인시스템 클래스명 | `AppTheme` -> `MyAppTheme` |
| Compose Resources import | `cmpsystem.core.xxx` -> `myapp.core.xxx` |
| iOS Bundle Identifier | `com.lwg.base.CmpSystem` -> `com.example.myapp.MyApp` |
| 소스 디렉터리 구조 | `com/lwg/base/` -> `com/example/myapp/` |

### 3. Gradle Sync & 빌드 확인

```bash
# 빌드 확인
./gradlew :composeApp:compileCommonMainKotlinMetadata

# Android 빌드
./gradlew :composeApp:compileDebugKotlinAndroid
```

Android Studio에서 `Invalidate Caches / Restart` 후 Gradle Sync를 실행하세요.

---

## 모듈 생성 스크립트

### Feature 모듈 생성

```bash
./scripts/generate-feature.sh <featureName>
```

**예시:**

```bash
./scripts/generate-feature.sh recipe
./scripts/generate-feature.sh RecipeDetail
```

생성되는 파일:
- `feature/<name>/build.gradle.kts`
- `State`, `Effect`, `ViewModel`, `Route`, `Screen`, `Koin Module`
- `settings.gradle.kts`에 모듈 자동 등록

생성 후 추가 작업:
1. Gradle Sync 실행
2. `core/navigation`에 Route 정의 추가
3. `feature/main`의 `MainScreen`에 NavDisplay 등록
4. `feature/main`의 `build.gradle.kts`에 의존성 추가

### Core 모듈 생성

```bash
./scripts/create-core-module.sh <moduleName> [타입]
```

**타입:**
- `android` (기본값) - Android Library + KMP + Compose
- `kotlin` - 순수 Kotlin Multiplatform

**예시:**

```bash
# Android Library 모듈
./scripts/create-core-module.sh data android

# 순수 Kotlin 모듈
./scripts/create-core-module.sh model kotlin

# 타입 생략 시 android 기본값
./scripts/create-core-module.sh ui
```

---

## 프로젝트 구조

```
:composeApp                    # 메인 앱 (Android/iOS 엔트리포인트)
:core:designsystem             # UI 컴포넌트, 테마, 디자인 토큰
:core:utils                    # 로깅, 유틸리티
:core:navigation               # Route 정의 (sealed interface)
:domain:model                  # Domain 모델
:domain:repository             # Repository 인터페이스
:domain:usecase                # UseCase 비즈니스 로직
:data:repositoryImpl           # Repository 구현체
:remote:network                # Ktor + Ktorfit 네트워크 클라이언트
:remote:api                    # API 인터페이스
:remote:model                  # DTO (서버 응답 모델)
:remote:mapper                 # DTO <-> Domain 모델 매퍼
:feature:main                  # 메인 Navigation 코디네이터 (BottomNav)
:feature:home                  # 홈 화면
:feature:{name}                # 각 기능별 Feature 모듈
```

## 기술 스택

- **Kotlin Multiplatform** (Android + iOS)
- **Compose Multiplatform** (UI)
- **Koin** (DI - KSP annotation 기반)
- **Ktor + Ktorfit** (네트워크)
- **Navigation 3** (NavDisplay + mutableStateListOf)
- **kotlinx.serialization** (직렬화)
