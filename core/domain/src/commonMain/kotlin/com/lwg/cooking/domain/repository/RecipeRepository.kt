package com.lwg.cooking.domain.repository

/**
 * Recipe 데이터를 관리하는 Repository 인터페이스
 *
 * Domain 계층에 위치:
 * - 순수한 비즈니스 로직 정의
 * - 구현체는 Data 계층에 위치
 * - 의존성 역전 원칙(DIP) 적용
 */
interface RecipeRepository {
    /**
     * 모든 레시피 목록 조회
     */
    suspend fun getRecipes(): Result<List<String>>

    /**
     * 특정 레시피 상세 정보 조회
     */
    suspend fun getRecipeById(id: String): Result<String>
}
