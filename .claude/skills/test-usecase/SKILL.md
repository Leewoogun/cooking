---
description: "UseCase 단위 테스트 코드 작성 - Repository Fake/Mock, Flow 검증, Validation 테스트 (Turbine, Coroutines Test)"
model: claude-sonnet-4-6
user-invocable: true
allowed-tools:
  - Read
  - Write
  - Edit
  - Glob
  - Grep
  - Bash
---

# UseCase Test Skill

$ARGUMENTS 를 기반으로 UseCase 단위 테스트를 작성합니다.

## 필요 인자

`$ARGUMENTS`에서 다음 정보를 파악합니다:
- **테스트 대상**: UseCase 클래스명 또는 기능명

## 사전 준비

### 1단계: 대상 UseCase 분석

대상 UseCase 파일을 읽고 다음을 파악합니다:
- 주입받는 Repository/UseCase 의존성
- `operator fun invoke()` 파라미터와 반환 타입
- Validation 로직, 비즈니스 로직
- Flow 반환 여부

**위치**: `core/domain/src/commonMain/kotlin/com/lwg/cooking/domain/usecase/{feature}/`

## 테스트 파일 작성

### 파일 위치

`core/domain/src/commonTest/kotlin/com/lwg/cooking/domain/usecase/{feature}/{UseCaseName}Test.kt`

### 기본 구조

```kotlin
package com.lwg.cooking.domain.usecase.{feature}

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class {UseCaseName}Test {

    private lateinit var fakeRepository: FakeSomeRepository
    private lateinit var useCase: {UseCaseName}

    @BeforeTest
    fun setup() {
        fakeRepository = FakeSomeRepository()
        useCase = {UseCaseName}(
            someRepository = fakeRepository,
        )
    }
}
```

> **참고**: KMP에서는 MockK 대신 **Fake 구현체** 사용을 권장합니다. MockK는 JVM 전용이므로 `commonTest`에서 사용 불가.

## 테스트 패턴

### 패턴 1: Flow 반환 UseCase (Turbine)

```kotlin
@Test
fun `빈 검색어면 빈 목록을 반환한다`() = runTest {
    useCase(query = "", onError = {}).test {
        val result = awaitItem()
        assertTrue(result.isEmpty())
        awaitComplete()
    }
}
```

### 패턴 2: suspend UseCase

```kotlin
@Test
fun `유효한 입력으로 호출 시 성공한다`() = runTest {
    fakeRepository.setResult(expectedData)

    var errorCalled = false
    val result = useCase(id = "123", onError = { errorCalled = true })

    assertEquals(expectedData, result)
    assertEquals(false, errorCalled)
}
```

## 테스트 네이밍 규칙

한글 백틱 네이밍을 사용합니다:
- `` `{조건}일 때 {기대 결과}` ``
- `` `{행위} 시 {기대 결과}` ``

## 체크리스트

- [ ] 테스트 파일 위치: `commonTest`에 있는지
- [ ] Fake Repository를 사용하는지 (MockK 대신)
- [ ] `runTest { }` 안에서 테스트가 실행되는지
- [ ] Flow 반환 UseCase는 `.test { }` (Turbine)으로 검증하는지
- [ ] 테스트 메서드명이 한글 백틱 네이밍인지
