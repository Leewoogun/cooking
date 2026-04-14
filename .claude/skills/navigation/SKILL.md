---
description: "새 Feature를 Navigation 3 시스템에 연결 - Route 정의, MainScreen NavDisplay 등록, build.gradle 의존성 수정"
model: claude-haiku-4-5-20251001
user-invocable: true
allowed-tools:
  - Read
  - Write
  - Edit
  - Glob
  - Grep
---

# Navigation 연결 Skill

$ARGUMENTS 를 기반으로 새 Feature를 Navigation 3 시스템에 연결합니다.

## 전제 조건

Feature 모듈이 이미 구현되어 있어야 합니다:
- `feature/{name}/` 디렉토리 존재
- `{Name}Screen.kt` Composable 존재

## 필요 인자

`$ARGUMENTS`에서 다음 정보를 파악합니다:
- **기능명**: PascalCase (예: `Recipe`, `Search`)
- **Route 타입**: `data object` (인자 없음) 또는 `data class` (인자 있음)
- **상위 Route 그룹**: 독립 Route 또는 기존 sealed interface 하위

## 수정 대상 파일 (4곳)

| 순서 | 파일 | 작업 |
|------|------|------|
| 1 | `core/navigation/.../Route.kt` | Route 정의 추가 |
| 2 | `feature/main/.../MainScreen.kt` | NavDisplay에 화면 등록 |
| 3 | `feature/main/build.gradle.kts` | Feature 모듈 의존성 추가 |
| 4 | `settings.gradle.kts` | 모듈 등록 확인 |

## 구현 순서

### 1단계: Route 정의 추가

**위치**: `core/navigation/src/commonMain/kotlin/com/lwg/cooking/navigation/Route.kt`

기존 `sealed interface Route`에 새 Route를 추가합니다.

**패턴 A - 독립 Route 그룹 (인자 없음)**:
```kotlin
sealed interface Route {
    // ... 기존 Route들 ...

    @Serializable
    sealed interface RecipeRoute : Route {
        @Serializable
        data object Main : RecipeRoute
    }
}
```

**패턴 B - 인자 있는 Route**:
```kotlin
@Serializable
sealed interface RecipeRoute : Route {
    @Serializable
    data object Main : RecipeRoute

    @Serializable
    data class Detail(val recipeId: Int) : RecipeRoute
}
```

**주의사항**:
- `@Serializable` 어노테이션 필수
- Navigation 3에서는 Route 객체가 backStack에 직접 들어감

### 2단계: MainScreen NavDisplay에 화면 등록

**위치**: `feature/main/src/commonMain/kotlin/com/lwg/cooking/feature/main/MainScreen.kt`

NavDisplay의 `entryDecorators` 또는 라우팅 로직에 새 화면을 등록합니다.

```kotlin
// NavDisplay 내부 when 분기에 추가
is Route.RecipeRoute.Main -> {
    RecipeScreen()
}
is Route.RecipeRoute.Detail -> { key ->
    RecipeDetailScreen(recipeId = key.recipeId)
}
```

**주의**: Feature 모듈의 Screen import 추가 필요

### 3단계: feature:main build.gradle.kts에 의존성 추가

**위치**: `feature/main/build.gradle.kts`

```kotlin
commonMain {
    dependencies {
        // ... 기존 의존성들 ...
        implementation(projects.feature.recipe)
    }
}
```

**네이밍 규칙**:
- 모듈명 `feature:recipe` → `projects.feature.recipe`
- 모듈명 `feature:recipe-detail` → `projects.feature.recipeDetail` (kebab → camelCase)

### 4단계: settings.gradle.kts 확인

```kotlin
include(":feature:recipe")
```

## 실제 파일 경로 요약

```
core/navigation/src/commonMain/kotlin/com/lwg/cooking/navigation/
└── Route.kt                              ← 1. Route 정의

feature/main/src/commonMain/kotlin/com/lwg/cooking/feature/main/
└── MainScreen.kt                         ← 2. NavDisplay 등록

feature/main/build.gradle.kts             ← 3. 모듈 의존성
settings.gradle.kts                       ← 4. 모듈 등록 확인
```

## 체크리스트

- [ ] `Route.kt`에 `@Serializable` Route 추가했는지
- [ ] `MainScreen.kt`의 NavDisplay에 화면 분기 추가했는지
- [ ] `feature/main/build.gradle.kts`에 의존성 추가했는지
- [ ] `settings.gradle.kts`에 모듈 등록되어 있는지
