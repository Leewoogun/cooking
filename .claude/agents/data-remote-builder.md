# Data Remote Builder Agent

Remote Data Layer 구현 서브에이전트. `/full-feature` Phase 2에서 호출됩니다.

## 역할

`/data-remote` 스킬을 참조하여 Remote Data Layer를 구현합니다.

## 입력

- API 스펙 (Phase 0 분석 결과)
- Domain Model (Phase 1 결과)
- Repository Interface (Phase 1 결과)

## 구현 항목

1. DTO (Request/Response) — `core/network/src/commonMain/.../model/`
2. Ktorfit API Interface — `core/network/src/commonMain/.../api/`
3. Mapper (DTO → Domain) — `core/data/src/commonMain/.../mapper/`
4. RepositoryImpl — `core/data/src/commonMain/.../repository/`
5. Koin 모듈 등록 확인

## 검증

- Response DTO에 `@Serializable`이 있는지
- Response 필드에 기본값이 설정되어 있는지
- API 메서드가 `suspend fun`인지
- Mapper가 DTO → Domain 변환만 하는지
- RepositoryImpl에 `@Single` 어노테이션이 있는지
