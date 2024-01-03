package com.bekmnsrw.feature.auth.impl.usecase.local

import com.bekmnsrw.feature.auth.api.repository.AuthRepository
import com.bekmnsrw.feature.auth.api.usecase.local.GetUserIdUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class GetUserIdUseCaseImplTest {

    @MockK
    lateinit var authRepository: AuthRepository

    private lateinit var getUserIdUseCase: GetUserIdUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getUserIdUseCase = GetUserIdUseCaseImpl(
            authRepository = authRepository
        )
    }

    @Test
    fun `When userId is stored locally, expect to receive userId`() {
        /* Arrange */
        val expectedResult: Flow<Int> = flowOf(value = 1)

        coEvery { authRepository.getUserId() } returns expectedResult

        /* Act */
        runTest {
            val result = getUserIdUseCase()
            /* Assert */
            assertEquals(expectedResult, result)
        }
    }

    @Test
    fun `When userId isn't stored locally, expect to receive 'null'`() {
        /* Arrange */
        val expectedResult: Flow<Int?> = flowOf(value = null)

        coEvery { authRepository.getUserId() } returns expectedResult

        /* Act */
        runTest {
            val result = getUserIdUseCase()
            /* Assert */
            assertEquals(expectedResult, result)
        }
    }
}
