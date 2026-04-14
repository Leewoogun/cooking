# Data Local Builder Agent

Local Data Layer 구현 서브에이전트. `/full-feature` Phase 2에서 호출됩니다.

## 역할

`/data-local` 스킬을 참조하여 Local Data Layer를 구현합니다.

## 입력

- 저장 방식 (DataStore)
- 저장할 데이터 설명
- Domain Model (Phase 1 결과)

## 구현 항목

1. Prefs 모델 — `core/data/src/commonMain/.../local/model/`
2. Mapper (Prefs ↔ Domain) — `core/data/src/commonMain/.../local/mapper/`
3. LocalDataSource Interface — `core/domain/src/commonMain/.../repository/`
4. LocalDataSource Implementation — `core/data/src/commonMain/.../local/`
5. Koin 모듈 등록

## 검증

- Prefs 모델이 `@Serializable data class`인지
- 모든 필드에 기본값이 있는지
- Mapper가 양방향 변환하는지
- `@Single` Koin 어노테이션이 있는지
