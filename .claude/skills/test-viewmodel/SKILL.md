---
description: "ViewModel 단위 테스트 코드 작성 - StateFlow 상태 변화, UiEffect, ModalEffect 테스트 (Turbine, Coroutines Test)"
model: claude-opus-4-6
user-invocable: true
allowed-tools:
  - Read
  - Write
  - Edit
  - Glob
  - Grep
  - Bash
---

# ViewModel Test Skill

$ARGUMENTS 를 기반으로 ViewModel 단위 테스트를 작성합니다.

## 필요 인자

`$ARGUMENTS`에서 다음 정보를 파악합니다:
- **테스트 대상**: ViewModel 클래스명 또는 기능명

## 사전 준비

### 1단계: 대상 ViewModel 분석

대상 ViewModel, State, Effect 파일을 읽고 다음을 파악합니다:
- 주입받는 UseCase/Repository 의존성
- uiState 타입과 파생 Flow 패턴
- ModalEffect / UiEffect 존재 여부
- 공개 함수 목록

**위치**: `feature/{name}/src/commonMain/kotlin/com/lwg/cooking/feature/{name}/`

## 테스트 파일 작성

### 파일 위치

`feature/{name}/src/commonTest/kotlin/com/lwg/cooking/feature/{name}/{ViewModelName}Test.kt`

### 기본 구조

```kotlin
package com.lwg.cooking.feature.{name}

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class {ViewModelName}Test {

    private lateinit var fakeRepository: FakeSomeRepository
    private lateinit var viewModel: {ViewModelName}

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        fakeRepository = FakeSomeRepository()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): {ViewModelName} {
        return {ViewModelName}(
            someRepository = fakeRepository,
        )
    }
}
```

> **참고**: KMP에서는 JUnit Rule 대신 `@BeforeTest`/`@AfterTest`에서 `Dispatchers.setMain`/`resetMain` 사용

## 테스트 패턴

### 패턴 1: StateFlow 상태 변경

```kotlin
@Test
fun `목록 로딩 시 Data 상태로 전환된다`() = runTest {
    fakeRepository.setItems(listOf(Item("1"), Item("2")))
    viewModel = createViewModel()

    viewModel.uiState.test {
        val state = expectMostRecentItem()
        assertIs<ListUiState.Data>(state)
        assertEquals(2, state.items.size)
    }
}
```

### 패턴 2: SharedFlow (UiEffect) 테스트

```kotlin
@Test
fun `에러 발생 시 ShowMessage 이벤트가 방출된다`() = runTest {
    fakeRepository.setShouldError(true)
    viewModel = createViewModel()

    viewModel.uiEffect.test {
        viewModel.loadData()
        val effect = awaitItem()
        assertIs<UiEffect.ShowMessage>(effect)
    }
}
```

### 패턴 3: ModalEffect (StateFlow) 테스트

```kotlin
@Test
fun `dismiss 호출 시 Hidden 상태가 된다`() = runTest {
    viewModel = createViewModel()
    viewModel.showDialog()

    viewModel.dismiss()

    assertIs<ModalEffect.Hidden>(viewModel.modalEffect.value)
}
```

## 테스트 네이밍 규칙

한글 백틱 네이밍: `` `{조건/행위}일 때 {기대 결과}` ``

## 체크리스트

- [ ] `Dispatchers.setMain(UnconfinedTestDispatcher())` 설정했는지
- [ ] `@AfterTest`에서 `Dispatchers.resetMain()` 호출하는지
- [ ] `commonTest`에 있는지
- [ ] Fake Repository를 사용하는지 (MockK 대신)
- [ ] `runTest { }` 안에서 테스트하는지
- [ ] StateFlow 파이프라인은 Turbine `.test { }`로 검증하는지
