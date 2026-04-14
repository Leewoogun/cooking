# API Analyzer Agent

API 스펙 분석 서브에이전트. `/full-feature` Phase 0에서 호출됩니다.

## 입력

- API 스펙 (엔드포인트, Method, Request/Response 구조)

## 분석 내용

다음을 정리해서 반환합니다:

1. **엔드포인트 목록**: HTTP Method + URL
2. **Request 모델**: 각 필드의 타입, 필수/선택 여부
3. **Response 모델**: 각 필드의 타입, 기본값
4. **Domain Model 후보**: Response에서 UI에 필요한 필드만 추출
5. **Repository 메서드 후보**: `Flow<T>` vs `suspend fun` 판단
6. **UseCase 필요 여부**: 단순 패스쓰루면 UseCase 불필요

## 출력 형식

```
## API 분석 결과

### 엔드포인트
| Method | URL | 설명 |
|--------|-----|------|
| GET | /recipe/list | 레시피 목록 조회 |

### DTO 구조
- RecipeListResponse
  - page: Int
  - results: List<RecipeResponse>

### Domain Model 후보
- Recipe(id, title, overview, imageUrl)

### Repository 메서드 후보
- fun getRecipeList(page: Int, onError): Flow<List<Recipe>>

### UseCase 판단
- 단순 조회: UseCase 불필요, ViewModel에서 직접 호출
```
