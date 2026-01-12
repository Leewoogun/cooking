# Network Module

Ktorfit과 Ktor Client를 사용한 네트워크 계층 모듈입니다.

## 역할

Network 모듈은 다음과 같은 역할을 담당합니다:

- **HTTP 통신**: Ktor Client를 사용한 멀티플랫폼 네트워크 통신
- **API 인터페이스 제공**: Ktorfit을 통한 타입 세이프한 API 정의
- **직렬화/역직렬화**: Kotlinx Serialization을 통한 JSON 처리
- **로깅**: HTTP 요청/응답 로깅

## 구조

```
core/network/
├── api/                 # Ktorfit API 인터페이스들
│   └── RecipeApi.kt
└── di/                 # Koin 모듈들
    ├── KtorfitModule.kt    # Ktorfit 기본 설정
    ├── ApiModule.kt        # API 인터페이스 제공
    └── NetworkModule.kt    # 통합 모듈
```

## Ktorfit과 Ktor Client

### Ktorfit이란?

Retrofit과 유사한 타입 세이프 HTTP 클라이언트 생성 도구입니다:

- 어노테이션 기반 API 정의
- KSP를 통한 구현 코드 자동 생성
- Kotlin Multiplatform 지원

### Ktor Client란?

Kotlin 멀티플랫폼 HTTP 엔진입니다:

- Android, iOS, Desktop 등 다양한 플랫폼 지원
- 플러그인 시스템 (ContentNegotiation, Logging 등)

### 둘의 관계

```
[Your Code]
    ↓
[Ktorfit Interface] (타입 세이프한 API 정의)
    ↓
[KSP Generated Code] (자동 생성된 구현)
    ↓
[Ktor Client] (실제 HTTP 요청 수행)
    ↓
[Network]
```

## 사용 방법

### 1. 새로운 API 추가하기

**Step 1: API 인터페이스 작성**

`api/UserApi.kt` 파일 생성:

```kotlin
package com.lwg.cooking.network.api

import de.jensklingenberg.ktorfit.http.*

interface UserApi {
    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: String): UserResponse

    @POST("users")
    suspend fun createUser(@Body user: CreateUserRequest): UserResponse
}
```

**Step 2: 프로젝트 빌드**

빌드하면 KSP가 자동으로 `Ktorfit.createUserApi()` extension function을 생성합니다.

**Step 3: ApiModule에 추가**

`di/ApiModule.kt`에 API를 등록:

```kotlin
val apiModule = module {
    single<RecipeApi> { get<Ktorfit>().createRecipeApi() }
    single<UserApi> { get<Ktorfit>().createUserApi() }  // ← 추가
}
```

### 2. Ktorfit 지원 어노테이션

#### HTTP 메서드
- `@GET`, `@POST`, `@PUT`, `@DELETE`, `@PATCH`, `@HEAD`, `@OPTIONS`

#### 파라미터
- `@Path("id")`: URL 경로 파라미터
- `@Query("name")`: 쿼리 파라미터
- `@QueryMap`: 쿼리 파라미터 맵
- `@Body`: Request Body
- `@Field("name")`: Form 필드
- `@Part`: Multipart 데이터
- `@Header("name")`: 개별 헤더
- `@Headers`: 다중 헤더

#### 예시

```kotlin
interface ExampleApi {
    // Path 파라미터
    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: String): User

    // Query 파라미터
    @GET("users")
    suspend fun searchUsers(
        @Query("name") name: String,
        @Query("limit") limit: Int = 10
    ): List<User>

    // Request Body
    @POST("users")
    suspend fun createUser(@Body user: CreateUserRequest): User

    // 헤더
    @GET("profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Profile

    // 다중 헤더
    @Headers("Accept: application/json", "User-Agent: MyApp")
    @GET("data")
    suspend fun getData(): Data
}
```

## 설정 커스터마이징

### Base URL 변경

`di/KtorfitModule.kt`의 `provideKtorfit()` 함수:

```kotlin
private fun provideKtorfit(httpClient: HttpClient): Ktorfit {
    return Ktorfit.Builder()
        .httpClient(httpClient)
        .baseUrl("https://your-api.com/")  // ← 변경
        .build()
}
```

### Logging Level 변경

`di/KtorfitModule.kt`의 `provideHttpClient()` 함수:

```kotlin
install(Logging) {
    level = LogLevel.BODY  // ALL, HEADERS, BODY, INFO, NONE
}
```

### Json 설정 변경

`di/KtorfitModule.kt`의 `provideJson()` 함수:

```kotlin
private fun provideJson(): Json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
    prettyPrint = true  // 프로덕션에서는 false 권장
}
```

### Timeout 설정

```kotlin
private fun provideHttpClient(json: Json): HttpClient {
    return HttpClient {
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
            connectTimeoutMillis = 15000
            socketTimeoutMillis = 15000
        }

        install(ContentNegotiation) {
            json(json)
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("HTTP Client: $message")
                }
            }
            level = LogLevel.ALL
        }
    }
}
```

## 모듈 구성

### KtorfitModule

Ktorfit 기본 설정을 제공합니다:

```kotlin
val ktorfitModule = module {
    single { provideJson() }           // Json 직렬화 설정
    single { provideHttpClient(get()) } // HttpClient 설정
    single { provideKtorfit(get()) }    // Ktorfit 인스턴스
}
```

**제공하는 것:**
- `Json`: Kotlinx Serialization 설정
- `HttpClient`: Ktor 클라이언트 (ContentNegotiation, Logging 포함)
- `Ktorfit`: API 인터페이스 생성을 위한 빌더

### ApiModule

API 인터페이스들을 제공합니다:

```kotlin
val apiModule = module {
    // KSP가 자동 생성한 extension function 사용
    single<RecipeApi> { get<Ktorfit>().createRecipeApi() }
    single<UserApi> { get<Ktorfit>().createUserApi() }
}
```

**왜 분리했나요?**
- API가 많아져도 관리하기 쉽습니다
- Ktorfit 설정과 API 인터페이스 관심사를 분리합니다
- 테스트 시 API만 모킹하기 편리합니다

### NetworkModule

모든 network 모듈을 통합합니다:

```kotlin
val networkModules = listOf(
    ktorfitModule,
    apiModule,
)
```

## 주의사항

### KSP 생성 함수 사용하기

❌ **Deprecated (사용하지 마세요):**
```kotlin
single { get<Ktorfit>().create<RecipeApi>() }
```

✅ **올바른 방법 (KSP가 생성한 extension function 사용):**
```kotlin
single<RecipeApi> { get<Ktorfit>().createRecipeApi() }
```

빌드 후 자동 완성을 통해 `createXxxApi()` 함수들을 확인할 수 있습니다.

### Data 모듈에서 사용하기

Network 모듈은 Data 모듈에서 의존합니다:

```kotlin
// Data 모듈의 build.gradle.kts
kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:network"))
        }
    }
}
```

따라서 Data 모듈의 Repository에서 API를 주입받아 사용할 수 있습니다:

```kotlin
class RecipeRepositoryImpl(
    private val recipeApi: RecipeApi  // ← Network 모듈에서 제공
) : RecipeRepository
```

## Koin 모듈 등록

Network 모듈은 보통 Data 모듈을 통해 간접적으로 등록됩니다:

```kotlin
// Data 모듈의 DataModule.kt
val dataModules = networkModules + listOf(repositoryModule)

// 앱 시작 시
startKoin {
    modules(dataModules)  // networkModules 포함
}
```

또는 직접 등록할 수도 있습니다:

```kotlin
startKoin {
    modules(networkModules)
}
```
