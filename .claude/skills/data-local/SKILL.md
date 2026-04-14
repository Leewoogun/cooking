---
description: "로컬 저장소 Data Layer 구현 - DataStore 기반 LocalDataSource 및 Koin 등록 (KMP)"
model: claude-sonnet-4-6
user-invocable: true
allowed-tools:
  - Read
  - Write
  - Edit
  - Glob
  - Grep
---

# Local Data Layer Skill

$ARGUMENTS 를 기반으로 Local Data Layer를 구현합니다.

## 필요 인자

`$ARGUMENTS`에서 다음 정보를 파악합니다:
- **기능명**: PascalCase (예: `Setting`, `Token`, `UserPreference`)
- **데이터 구조**: 저장할 필드

## DataStore 구현 순서

### 1단계: Prefs 모델 정의

**위치**: `core/data/src/commonMain/kotlin/com/lwg/cooking/data/local/model/`

**규칙**:
- `@Serializable data class`
- 모든 필드에 기본값 설정
- `commonMain`에 작성

```kotlin
package com.lwg.cooking.data.local.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPrefs(
    val token: String = "",
    val isAutoLogin: Boolean = false,
    val userName: String = "",
)
```

### 2단계: Prefs Mapper (Prefs ↔ Domain)

> Domain Model이 없으면 이 단계를 건너뜁니다.

**위치**: `core/data/src/commonMain/kotlin/com/lwg/cooking/data/local/mapper/`

```kotlin
package com.lwg.cooking.data.local.mapper

import com.lwg.cooking.data.local.model.UserPrefs
import com.lwg.cooking.domain.model.user.UserInfo

fun UserPrefs.toUserInfo(): UserInfo {
    return UserInfo(
        name = userName,
        isAutoLogin = isAutoLogin,
    )
}

fun UserInfo.toUserPrefs(): UserPrefs {
    return UserPrefs(
        userName = name,
        isAutoLogin = isAutoLogin,
    )
}
```

### 3단계: LocalDataSource 인터페이스

**위치**: `core/domain/src/commonMain/kotlin/com/lwg/cooking/domain/repository/` 또는 별도 인터페이스

```kotlin
interface UserLocalRepository {
    fun observeUser(): Flow<UserInfo?>
    suspend fun getUser(): UserInfo?
    suspend fun saveUser(userInfo: UserInfo)
    suspend fun deleteUser()
}
```

### 4단계: LocalDataSource 구현

**위치**: `core/data/src/commonMain/kotlin/com/lwg/cooking/data/local/`

**규칙**:
- `@Single` Koin annotation 사용
- DataStore 주입
- Flow 조회: `.data.map { prefs -> prefs.toDomain() }` 파이프라인

```kotlin
package com.lwg.cooking.data.local

import com.lwg.cooking.data.local.mapper.toUserInfo
import com.lwg.cooking.data.local.mapper.toUserPrefs
import com.lwg.cooking.data.local.model.UserPrefs
import com.lwg.cooking.domain.model.user.UserInfo
import com.lwg.cooking.domain.repository.UserLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class UserLocalRepositoryImpl(
    private val dataStore: DataStore<UserPrefs>,
) : UserLocalRepository {

    override fun observeUser(): Flow<UserInfo?> {
        return dataStore.data.map { prefs ->
            if (prefs.userName.isEmpty()) null else prefs.toUserInfo()
        }
    }

    override suspend fun saveUser(userInfo: UserInfo) {
        dataStore.updateData { userInfo.toUserPrefs() }
    }
}
```

## 체크리스트

- [ ] Prefs 모델이 `@Serializable data class`인지
- [ ] 모든 필드에 기본값이 있는지
- [ ] Mapper가 양방향 변환하는지 (Domain 존재 시)
- [ ] DataSource 구현체에 `@Single` Koin 어노테이션이 있는지
- [ ] `commonMain`에 작성했는지
