---
description: "버그 수정 워크플로우 - 이슈 분석 → 원인 코드 탐색 → 영향 범위 파악 → 수정 → 검증. 버그, 에러, 크래시, 오류 수정, 문제 해결 요청 시 사용."
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
---

# 버그 수정 Skill

$ARGUMENTS 를 기반으로 버그의 원인을 분석하고 수정한다.

## 필요 인자

`$ARGUMENTS`에서 다음 정보를 파악한다:
- **증상**: 어떤 문제가 발생하는지 (크래시, 잘못된 데이터, UI 오류 등)
- **재현 조건** (선택): 어떤 상황에서 발생하는지
- **관련 화면/기능** (선택): 어느 기능에서 발생하는지
- **에러 로그** (선택): 스택트레이스, 로그 메시지

정보가 부족하면 사용자에게 질문한다.

## 수정 플로우

```
Step 1: 증상 분석 — 에러 유형과 발생 지점 파악
    ↓
Step 2: 원인 코드 탐색 — 관련 코드 추적
    ↓
Step 3: 영향 범위 파악 — 수정 시 영향받는 다른 코드 확인
    ↓
Step 4: 수정 — 최소 범위 수정
    ↓
Step 5: 검증 — 수정 후 빌드 확인
```

## 에러 유형별 진입점

| 에러 유형 | 탐색 시작점 |
|-----------|------------|
| 크래시 (NPE, ClassCast 등) | 스택트레이스의 최상위 프로젝트 코드 |
| 잘못된 데이터 표시 | Screen → UiState → ViewModel → UseCase → Repository 역추적 |
| API 호출 실패 | RepositoryImpl → API → Mapper 순서 확인 |
| Navigation 오류 | Route → MainScreen NavDisplay 확인 |
| 상태 멈춤 (로딩 무한) | ViewModel의 Flow 파이프라인 catch 누락, stateIn 초기값 확인 |
| Koin DI 에러 | Module 등록 누락, @Single/@KoinViewModel 확인 |

## 수정 원칙

1. **최소 범위 수정**: 버그 원인만 정확히 수정
2. **기존 패턴 유지**: 기존 코드의 패턴과 스타일을 따른다
3. **프로젝트 컨벤션 준수**: `CLAUDE.md`의 컨벤션 규칙을 따른다

## 빌드 검증

```bash
./gradlew :{변경모듈}:compileCommonMainKotlinMetadata
```

## 결과 출력

```
## 버그 수정 결과

### 증상
{버그 증상 설명}

### 원인
{근본 원인 설명}

### 수정 내용
| 파일 | 변경 내용 |
|------|----------|
| {파일명} | {변경 설명} |

### 빌드 결과
- {빌드 성공/실패}
```
