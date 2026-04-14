---
description: "기존 기능 수정 워크플로우 - 영향 레이어 분석 → 변경 계획 → 레이어별 수정 → 검증. 기존 화면 수정, 기능 변경, 필드 추가/삭제, API 변경 반영, 로직 변경 요청 시 사용."
model: claude-opus-4-6
user-invocable: true
allowed-tools:
  - Agent
  - Read
  - Write
  - Edit
  - Glob
  - Grep
  - Bash
  - Skill
  - mcp__ide__getDiagnostics
  - mcp__figma-remote-mcp__get_screenshot
  - mcp__figma-remote-mcp__get_design_context
---

# 기존 기능 수정 Skill

$ARGUMENTS 를 기반으로 기존 기능을 수정한다. 신규 생성(`/full-feature`)과 달리, 이미 존재하는 코드의 영향 범위를 파악하고 레이어별로 안전하게 변경한다.

## 필요 인자

`$ARGUMENTS`에서 다음 정보를 파악한다:
- **대상 기능**: 수정할 기능명 또는 화면명
- **변경 내용**: 무엇을 어떻게 변경하는지
- **Figma URL** (선택): 변경된 디자인이 있는 경우
- **API 변경 스펙** (선택): API가 변경된 경우

## 수정 플로우

```
Step 1: 현재 구조 파악 — 대상 기능의 전체 레이어 코드 탐색
    ↓
Step 2: 영향 레이어 분석 — 어떤 레이어가 변경되어야 하는지 판별
    ↓
Step 3: 변경 계획 수립 — 레이어별 수정 항목 목록 (사용자 확인)
    ↓
Step 4: 레이어별 수정 — 의존 방향 순서대로 수정
    ↓
Step 5: 경계면 검증 — QA 에이전트 또는 수동 교차 비교
    ↓
Step 6: 빌드 검증
```

## 영향 레이어 분석

| 변경 유형 | 영향 레이어 |
|-----------|------------|
| **API Response 필드 추가** | DTO → Mapper → Domain Model → UiState → Screen |
| **API 엔드포인트 변경** | API Interface → RepositoryImpl |
| **새 API 추가** | DTO, API, Mapper, Repository IF/Impl, (UseCase), ViewModel |
| **비즈니스 로직 변경** | UseCase → ViewModel |
| **UI 디자인 변경** | Screen → (UiState) |
| **Navigation 인자 변경** | Route → Screen |

## 수정 원칙

1. **기존 패턴 유지**: 기존 코드의 패턴과 스타일을 따른다
2. **최소 변경**: 요청된 변경만 수행한다. 주변 코드 리팩토링/개선 금지
3. **프로젝트 컨벤션 준수**: `CLAUDE.md`의 컨벤션 규칙을 따른다

## 수정 시 참조할 스킬

| 레이어 | 참조 스킬 |
|--------|----------|
| Domain | `/domain` |
| Data Remote | `/data-remote` |
| Data Local | `/data-local` |
| ViewModel | `/viewmodel` |
| Screen UI | `design-system` (자동 적용) |

## 빌드 검증

```bash
./gradlew :{변경모듈}:compileCommonMainKotlinMetadata
```
