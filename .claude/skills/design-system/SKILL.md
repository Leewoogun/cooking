---
description: "Compose UI 코드 작성 시 자동 참조되는 디자인 시스템 규칙 (프로젝트 테마 색상, 타이포, 컴포넌트 매핑)"
user-invocable: false
allowed-tools:
  - Read
  - Glob
  - Grep
---

# 디자인 시스템 핵심 규칙

UI Composable 코드를 작성할 때 이 규칙을 반드시 따릅니다.

## 색상 규칙

프로젝트의 디자인 시스템 테마를 통해서만 색상에 접근합니다.
`core/designsystem/` 디렉토리의 테마 파일을 확인하여 사용 가능한 색상을 파악합니다.

```kotlin
// ❌ 절대 금지
Color(0xFF000000)                       // 하드코딩 금지
```

**새 색상이 필요한 경우만 임시 하드코딩 허용**:
```kotlin
// TODO: 새로운 색상 필요 - 디자인 시스템에 추가 요청
// 용도: [사용 목적]
// 제안 색상: Color(0xFFXXXXXX)
Box(modifier = Modifier.background(Color(0xFFXXXXXX)))
```

## 타이포그래피 규칙

프로젝트 디자인 시스템의 Typography를 사용합니다.
`core/designsystem/`의 Typography 정의를 확인하여 사용 가능한 스타일을 파악합니다.

```kotlin
// ❌ 금지 (직접 fontSize/fontWeight 조합)
fontSize = 20.sp, fontWeight = FontWeight.Bold
```

## 컴포넌트 규칙

`core/designsystem/` 디렉토리에 정의된 공용 컴포넌트를 우선 사용합니다.
커스텀 UI가 필요한 경우에만 직접 구현합니다.

## UI 작성 규칙

1. **KMP Compose**: `commonMain`에 작성 (플랫폼 독립)
2. **클릭 영역**: `clip(RoundedCornerShape(4.dp))` 처리
3. **다크 모드**: 디자인 시스템 테마 사용 시 자동 대응

## 코드 작성 전 확인

**반드시 `core/designsystem/` 디렉토리를 먼저 탐색**하여 다음을 확인합니다:
- 테마 (색상, 타이포)
- 기존 공용 컴포넌트
- 기존 패턴과 네이밍 컨벤션

이렇게 하면 아직 정의되지 않은 디자인 시스템도 코드를 작성하면서 점진적으로 파악할 수 있습니다.
