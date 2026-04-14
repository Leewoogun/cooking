---
description: "서버 API 연동 Data Layer 구현 - DTO, Ktorfit API, Mapper, RepositoryImpl, Koin 모듈 등록 (KMP)"
model: claude-sonnet-4-6
user-invocable: true
allowed-tools:
  - Read
  - Write
  - Edit
  - Glob
  - Grep
---

# Remote Data Layer Skill

$ARGUMENTS 를 기반으로 Remote API Data Layer를 구현합니다.

## 필요 인자

`$ARGUMENTS`에서 다음 정보를 파악합니다:
- **기능명**: PascalCase (예: `Recipe`, `Search`, `Auth`)
- **API 스펙**: 엔드포인트, HTTP 메서드, Request/Response 구조

## 모듈 아키텍처

```
:core:network          # Ktorfit API 인터페이스, DTO, 네트워크 설정
                       # 의존: ktor, ktorfit, kotlinx.serialization, koin
:core:data             # Repository 구현체, Mapper, Koin 모듈
                       # 의존: :core:domain, :core:network, koin
:core:domain           # Repository 인터페이스, Domain Model
```

## 구현 순서

### 1단계: DTO 모델 (Request/Response)

**위치**: `core/network/src/commonMain/kotlin/com/lwg/cooking/network/model/{feature}/`

**규칙**:
- `@Serializable` 어노테이션 필수
- `commonMain`에 작성
- **Response 필드에 기본값 설정** (null → 기본값 변환)
- snake_case 필드명은 `@SerialName` 사용

```kotlin
package com.lwg.cooking.network.model.recipe

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeListResponse(
    val page: Int = 0,
    val results: List<RecipeResponse> = emptyList(),
    @SerialName("total_pages")
    val totalPages: Int = 0,
)

@Serializable
data class RecipeResponse(
    val id: Int = 0,
    val title: String = "",
    val overview: String = "",
    @SerialName("poster_path")
    val posterPath: String? = null,
)
```

### 2단계: Ktorfit API 인터페이스

**위치**: `core/network/src/commonMain/kotlin/com/lwg/cooking/network/api/`

**규칙**:
- `interface` + Ktorfit 어노테이션 사용
- `suspend fun` 사용
- 패키지: `com.lwg.cooking.network.api`

```kotlin
package com.lwg.cooking.network.api

import com.lwg.cooking.network.model.recipe.RecipeListResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface RecipeApi {

    @GET("recipe/list")
    suspend fun getRecipeList(
        @Query("page") page: Int = 1,
    ): RecipeListResponse
}
```

### 3단계: Mapper (DTO → Domain)

**위치**: `core/data/src/commonMain/kotlin/com/lwg/cooking/data/mapper/`

**규칙**:
- 확장 함수로 작성
- 네이밍: `fun {Response}.to{DomainModel}(): DomainModel`
- 패키지: `com.lwg.cooking.data.mapper`

```kotlin
package com.lwg.cooking.data.mapper

import com.lwg.cooking.domain.model.recipe.Recipe
import com.lwg.cooking.network.model.recipe.RecipeResponse

fun RecipeResponse.toRecipe(): Recipe {
    return Recipe(
        id = id,
        title = title,
        overview = overview,
        imageUrl = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
    )
}
```

### 4단계: RepositoryImpl 구현

**위치**: `core/data/src/commonMain/kotlin/com/lwg/cooking/data/repository/`

**규칙**:
- `@Single` Koin annotation 사용
- API 인터페이스 주입
- 패키지: `com.lwg.cooking.data.repository`

```kotlin
package com.lwg.cooking.data.repository

import com.lwg.cooking.data.mapper.toRecipe
import com.lwg.cooking.domain.model.recipe.Recipe
import com.lwg.cooking.domain.repository.RecipeRepository
import com.lwg.cooking.network.api.RecipeApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class RecipeRepositoryImpl(
    private val recipeApi: RecipeApi,
) : RecipeRepository {

    override fun getRecipeList(
        page: Int,
        onError: (String) -> Unit,
    ): Flow<List<Recipe>> = flow {
        try {
            val response = recipeApi.getRecipeList(page)
            emit(response.results.map { it.toRecipe() })
        } catch (e: Exception) {
            onError(e.message ?: "네트워크 오류가 발생했습니다.")
        }
    }
}
```

### 5단계: Koin 모듈 등록

**위치**: `core/data/src/commonMain/kotlin/com/lwg/cooking/data/di/DataModule.kt`

기존 DataModule에 `@ComponentScan`이 설정되어 있으면 `@Single` 어노테이션으로 자동 등록됩니다.
없으면 모듈 파일을 생성합니다:

```kotlin
package com.lwg.cooking.data.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("com.lwg.cooking.data")
class DataModule
```

API 인터페이스의 Koin 등록은 `core/network`의 NetworkModule에서 Ktorfit builder로 제공합니다.

## 빌드 검증

```bash
./gradlew :core:network:compileCommonMainKotlinMetadata :core:data:compileCommonMainKotlinMetadata
```

## 체크리스트

- [ ] DTO가 `@Serializable`이고 `commonMain`에 있는지
- [ ] Response 필드에 기본값이 설정되어 있는지
- [ ] API 인터페이스가 Ktorfit 어노테이션을 사용하는지
- [ ] Mapper가 DTO → Domain 변환만 하는지
- [ ] RepositoryImpl에 `@Single` Koin 어노테이션이 있는지
- [ ] Koin 모듈에 등록되었는지
