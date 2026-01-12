# Domain Module

순수한 비즈니스 로직을 담당하는 Domain 계층 모듈입니다.

## 역할

Domain 모듈은 Clean Architecture의 가장 안쪽 계층으로, 다음과 같은 특징을 가집니다:

- **순수 Kotlin**: 플랫폼 의존성이 없는 순수 Kotlin 코드
- **비즈니스 로직**: 앱의 핵심 비즈니스 규칙 정의
- **의존성 없음**: 다른 모듈에 의존하지 않음 (Coroutines만 의존)
- **인터페이스 정의**: Repository, UseCase 등의 인터페이스 정의

## 의존성 구조 (Clean Architecture)

```
Domain (가장 안쪽 - 의존성 없음)
    ↑
Data (Domain에 의존)
    ↑
Presentation (Data/Domain에 의존)
```

## Repository 인터페이스

### 왜 Domain에 있나요?

**의존성 역전 원칙 (Dependency Inversion Principle):**

- **인터페이스는 Domain에**: 순수한 비즈니스 로직 정의
- **구현체는 Data에**: 실제 데이터 접근 로직

### 예시

```kotlin
// Domain 모듈: 인터페이스
interface RecipeRepository {
    suspend fun getRecipes(): Result<List<String>>
}

// Data 모듈: 구현체
class RecipeRepositoryImpl(
    private val recipeApi: RecipeApi
) : RecipeRepository {
    override suspend fun getRecipes(): Result<List<String>> {
        return try {
            Result.success(recipeApi.getRecipes())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// ViewModel: Domain 인터페이스에만 의존
class RecipeViewModel(
    private val repository: RecipeRepository
) : ViewModel()
```

## 새로운 Repository 추가

1. **Domain**: `repository/UserRepository.kt` 인터페이스 작성
2. **Data**: `repository/UserRepositoryImpl.kt` 구현체 작성
3. **Koin**: `RepositoryModule`에 등록

자세한 내용은 `core/data/README.md`를 참고하세요.
