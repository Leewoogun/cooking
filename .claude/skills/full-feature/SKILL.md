---
description: "새 기능을 처음부터 끝까지 완전 구현 - Figma 분석 + API 스펙 파악 → Domain → Data → Feature → Navigation 연결까지 전체 레이어 순차 생성"
model: claude-opus-4-6
user-invocable: true
allowed-tools:
  - Agent
  - Read
  - Write
  - Edit
  - Glob
  - Grep
  - Skill
  - mcp__figma-remote-mcp__get_screenshot
  - mcp__figma-remote-mcp__get_design_context
  - mcp__figma-remote-mcp__get_variable_defs
  - mcp__figma-remote-mcp__get_metadata
---

# 전체 기능 구현 Skill

$ARGUMENTS 를 기반으로 하나의 기능을 Figma 분석부터 Navigation 연결까지 완전히 구현합니다.

## 필요 인자

`$ARGUMENTS`에서 다음 정보를 파악합니다:
- **기능명**: PascalCase (예: `Recipe`, `Search`, `Profile`)
- **Figma URL** (선택): 디자인 링크
- **API 스펙** (선택): 엔드포인트, Request/Response
- **비즈니스 요구사항**: 데이터 구조, 비즈니스 로직
- **로컬 저장**: DataStore 필요 여부
- **UI 요구사항**: 화면 구성, 사용자 인터랙션

인자가 불충분한 경우 사용자에게 질문하여 확인합니다.

---

## 전체 구현 플로우

```
Phase 0: 입력 분석 — Figma 디자인 분석 + API 스펙 파악 (서브에이전트 병렬)
    ↓
Phase 1: Domain Layer — Model, Repository Interface, UseCase
    ↓
Phase 2: Data Layer — DTO, API, Mapper, DataSource, RepositoryImpl (서브에이전트 병렬)
    ↓
Phase 3: Feature Layer — State, Effect, ViewModel, Screen, Navigation
    ↓
Phase 4: Navigation 연결
    ↓
Phase 5: 검증
```

---

## Phase 0: 입력 분석 (서브에이전트 병렬 실행)

Figma URL과 API 스펙이 모두 제공된 경우, **두 작업을 서브에이전트로 동시에 실행**합니다.

### Agent A: Figma 디자인 분석

Figma URL이 제공된 경우에만 실행합니다.
`.claude/agents/figma-analyzer.md`를 읽고 서브에이전트로 호출합니다.

### Agent B: API 스펙 분석

API 스펙이 제공된 경우에만 실행합니다.
`.claude/agents/api-analyzer.md`를 읽고 서브에이전트로 호출합니다.

### Figma/API 중 하나만 제공된 경우

- **Figma만 있음**: Agent A만 실행
- **API만 있음**: Agent B만 실행
- **둘 다 없음**: Phase 0 건너뛰고 $ARGUMENTS의 비즈니스 요구사항으로 Phase 1 시작

---

## Phase 1: Domain Layer

`/domain` 스킬에 위임합니다.

1. **Domain Model** — API Response에서 UI에 필요한 필드만 추출
2. **Repository Interface** — 데이터 접근 계약 정의
3. **UseCase** — 비즈니스 로직 (필요한 경우에만)

---

## Phase 2: Data Layer (서브에이전트 병렬 실행)

Domain Layer가 완료되면, Remote와 Local을 **서브에이전트로 동시에 실행**합니다.

### Agent C: Remote Data Layer

API가 있는 경우 `.claude/agents/data-remote-builder.md`를 읽고 서브에이전트로 호출합니다.

### Agent D: Local Data Layer

로컬 저장이 필요한 경우 `.claude/agents/data-local-builder.md`를 읽고 서브에이전트로 호출합니다.

### RepositoryImpl (서브에이전트 완료 후)

Agent C, D가 완료되면 RepositoryImpl을 구현합니다.

---

## Phase 3: Feature Layer

`/feature` 스킬에 위임합니다. ViewModel은 `/viewmodel` 스킬에 재위임됩니다.

---

## Phase 4: Navigation 연결

`/navigation` 스킬에 위임합니다.

---

## Phase 간 빌드 검증

각 Phase 완료 후 빌드를 확인합니다:

| Phase 완료 | 빌드 명령 |
|-----------|----------|
| Phase 1 (Domain) | `./gradlew :core:domain:compileCommonMainKotlinMetadata` |
| Phase 2 (Data) | `./gradlew :core:data:compileCommonMainKotlinMetadata :core:network:compileCommonMainKotlinMetadata` |
| Phase 3 (Feature) | `./gradlew :feature:{기능모듈}:compileCommonMainKotlinMetadata` |
| Phase 4 (Navigation) | `./gradlew :feature:main:compileCommonMainKotlinMetadata` |

---

## Phase 5: 경계면 통합 검증 (QA)

`qa-integration` 에이전트를 호출하여 경계면 교차 비교를 수행합니다.

```
Agent tool 호출:
.claude/agents/qa-integration.md를 읽고 {기능명}에 대해 경계면 검증 실행
```

---

## 스킬 위임 요약

| Phase | 위임 스킬 | 비고 |
|-------|----------|------|
| 0 | `/figma-ui` (Agent A) | Figma URL 있을 때 |
| 1 | `/domain` | Domain Model, Repository IF, UseCase |
| 2 | `/data-remote` (Agent C) | DTO, API, Mapper, RemoteDataSource |
| 2 | `/data-local` (Agent D) | DataStore |
| 3 | `/feature` → `/viewmodel` | State, Effect, ViewModel, Screen |
| 4 | `/navigation` | Route 등록, MainScreen 수정 |
