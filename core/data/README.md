# Data Module

Repository 패턴을 사용한 데이터 계층 모듈입니다.

## 역할

Data 모듈은 다음과 같은 역할을 담당합니다:

- **데이터 소스 추상화**: 네트워크, 로컬 DB 등 다양한 데이터 소스를 추상화
- **비즈니스 로직 처리**: API 응답을 Domain 모델로 변환
- **에러 처리**: 네트워크 에러, 파싱 에러 등을 Result로 래핑
- **캐싱 전략**: 메모리 캐시, 디스크 캐시 등 구현

## 의존성 (Clean Architecture)

Data 모듈은 Domain과 Network 모듈에 의존합니다:

```
Data Module
    ↓ (의존)
Domain Module (Repository 인터페이스)
    +
Network Module (Ktorfit API)
```

- `api(project(":core:domain"))`: Repository 인터페이스를 구현
- `implementation(project(":core:network"))`: API를 사용하여 데이터 로드

Domain을 `api`로 의존하므로, Data 모듈을 사용하는 곳에서도 Repository 인터페이스에 접근할 수 있습니다.

## 구조

```
core/data/
├── repository/           # Repository 구현체들
│   └── RecipeRepository.kt
└── di/                  # Koin 모듈들
    ├── RepositoryModule.kt
    └── DataModule.kt
```

## Repository 패턴

### Repository란?

Repository는 데이터 계층과 Domain 계층 사이의 중재자입니다:

```
[UI/ViewModel]
    ↓
[Domain/UseCase]
    ↓
[Repository] ← 여기서 데이터 소스 추상화
    ↓
[Network API / Local DB]
```

### 구현 예시

**1. Repository 인터페이스 정의 (Domain 모듈)**

```kotlin
// core/domain/repository/RecipeRepository.kt
package com.lwg.cooking.domain.repository

interface RecipeRepository {
    suspend fun getRecipes(): Result<List<Recipe>>
    suspend fun getRecipeById(id: String): Result<Recipe>
}
```

**2. Repository 구현체 작성 (Data 모듈)**

```kotlin
// core/data/repository/RecipeRepositoryImpl.kt
package com.lwg.cooking.data.repository

import com.lwg.cooking.domain.repository.RecipeRepository
import com.lwg.cooking.network.api.RecipeApi

class RecipeRepositoryImpl(
    private val recipeApi: RecipeApi  // Network 모듈에서 주입
) : RecipeRepository {

    override suspend fun getRecipes(): Result<List<Recipe>> {
        return try {
            val recipes = recipeApi.getRecipes()
            Result.success(recipes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRecipeById(id: String): Result<Recipe> {
        return try {
            val recipe = recipeApi.getRecipeById(id)
            Result.success(recipe)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

**3. Koin 모듈에 등록 (Data 모듈)**

```kotlin
// core/data/di/RepositoryModule.kt
val repositoryModule = module {
    singleOf(::RecipeRepositoryImpl) { bind<RecipeRepository>() }
}
```

## 사용 방법

### 1. Koin 모듈 등록

앱 시작 시 `dataModules`를 등록합니다:

```kotlin
startKoin {
    modules(dataModules)  // networkModules + repositoryModule 포함
}
```

### 2. ViewModel/UseCase에서 Repository 주입

```kotlin
class RecipeViewModel(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    fun loadRecipes() {
        viewModelScope.launch {
            recipeRepository.getRecipes()
                .onSuccess { recipes ->
                    // 성공 처리
                }
                .onFailure { error ->
                    // 에러 처리
                }
        }
    }
}

// Koin 모듈
val viewModelModule = module {
    viewModelOf(::RecipeViewModel)
}
```

### 3. 새로운 Repository 추가하기

**Step 1: Domain 모듈에 인터페이스 작성**

`core/domain/repository/UserRepository.kt`:

```kotlin
package com.lwg.cooking.domain.repository

interface UserRepository {
    suspend fun getUser(id: String): Result<User>
}
```

**Step 2: Data 모듈에 구현체 작성**

`core/data/repository/UserRepositoryImpl.kt`:

```kotlin
package com.lwg.cooking.data.repository

import com.lwg.cooking.domain.repository.UserRepository
import com.lwg.cooking.network.api.UserApi

class UserRepositoryImpl(
    private val userApi: UserApi  // Network 모듈에서 제공
) : UserRepository {

    override suspend fun getUser(id: String): Result<User> {
        return try {
            val user = userApi.getUser(id)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

**Step 3: RepositoryModule에 등록**

`core/data/di/RepositoryModule.kt`:

```kotlin
val repositoryModule = module {
    singleOf(::RecipeRepositoryImpl) { bind<RecipeRepository>() }
    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }  // ← 추가
}
```

## Repository에서 할 수 있는 일

### 1. 에러 처리 및 Result 래핑

```kotlin
override suspend fun getRecipes(): Result<List<Recipe>> {
    return try {
        val response = recipeApi.getRecipes()
        Result.success(response)
    } catch (e: HttpException) {
        Result.failure(NetworkError.HttpError(e))
    } catch (e: IOException) {
        Result.failure(NetworkError.NoInternet)
    }
}
```

### 2. 데이터 변환 (DTO → Domain Model)

```kotlin
override suspend fun getRecipes(): Result<List<Recipe>> {
    return try {
        val dtoList = recipeApi.getRecipes()
        val domainList = dtoList.map { it.toDomain() }
        Result.success(domainList)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

### 3. 캐싱 전략

```kotlin
class RecipeRepositoryImpl(
    private val recipeApi: RecipeApi,
    private val recipeCache: RecipeCache  // 로컬 캐시
) : RecipeRepository {

    override suspend fun getRecipes(): Result<List<Recipe>> {
        return try {
            // 1. 캐시 확인
            val cached = recipeCache.getRecipes()
            if (cached.isNotEmpty()) {
                return Result.success(cached)
            }

            // 2. 네트워크 요청
            val recipes = recipeApi.getRecipes()

            // 3. 캐시 저장
            recipeCache.saveRecipes(recipes)

            Result.success(recipes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### 4. 여러 데이터 소스 결합

```kotlin
override suspend fun getRecipe(id: String): Result<Recipe> {
    return try {
        // 로컬 DB와 네트워크 데이터를 결합
        val localData = localDatabase.getRecipe(id)
        val remoteData = recipeApi.getRecipeById(id)

        val merged = mergeRecipeData(localData, remoteData)
        Result.success(merged)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

## 의존성 주입 흐름

```
startKoin {
    modules(dataModules)
}
    ↓
dataModules = networkModules + repositoryModule
    ↓
networkModules = [ktorfitModule, apiModule]
    ↓
ktorfitModule → HttpClient, Ktorfit 제공
apiModule → RecipeApi, UserApi 등 제공 (Network 모듈)
    ↓
repositoryModule → RecipeRepositoryImpl 등 제공 (Data 모듈)
    (Domain의 RecipeRepository 인터페이스를 구현)
    (Network의 RecipeApi를 주입받음)
    ↓
ViewModel/UseCase → Domain의 RecipeRepository 인터페이스를 주입받음
    (실제로는 Data의 RecipeRepositoryImpl이 주입됨)
```

## Clean Architecture 구조

```
Presentation (ViewModel)
    ↓ 의존
Domain (Repository Interface)
    ↑ 구현
Data (Repository Impl)
    ↓ 사용
Network (API)
```

**의존성 역전 원칙:**
- ViewModel은 Domain의 인터페이스만 알면 됨
- Data 모듈이 Domain의 인터페이스를 구현
- 변경에 유연함 (구현체를 쉽게 교체 가능)

## 주의사항

### Repository 인터페이스는 Domain에 위치

```kotlin
// ❌ 잘못된 위치
// core/data/repository/RecipeRepository.kt
interface RecipeRepository

// ✅ 올바른 위치
// core/domain/repository/RecipeRepository.kt
interface RecipeRepository
```

### Repository 구현체는 Data에 위치

```kotlin
// core/data/repository/RecipeRepositoryImpl.kt
import com.lwg.cooking.domain.repository.RecipeRepository  // Domain 인터페이스
import com.lwg.cooking.network.api.RecipeApi              // Network API

class RecipeRepositoryImpl(
    private val recipeApi: RecipeApi  // Network 모듈에서 자동 주입
) : RecipeRepository  // Domain 인터페이스 구현
```

### Result 타입 사용 권장

비동기 작업의 성공/실패를 명확히 하기 위해 Kotlin의 `Result` 타입 사용을 권장합니다:

```kotlin
suspend fun getRecipes(): Result<List<Recipe>>
```

사용하는 쪽에서:

```kotlin
repository.getRecipes()
    .onSuccess { recipes -> /* 성공 */ }
    .onFailure { error -> /* 실패 */ }
```
