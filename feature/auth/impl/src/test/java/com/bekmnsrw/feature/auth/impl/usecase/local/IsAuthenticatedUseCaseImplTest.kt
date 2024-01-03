package com.bekmnsrw.feature.auth.impl.usecase.local

import com.bekmnsrw.feature.auth.api.repository.AuthRepository
import com.bekmnsrw.feature.auth.api.usecase.local.IsAuthenticatedUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class IsAuthenticatedUseCaseImplTest {

    @MockK
    lateinit var authRepository: AuthRepository

    private lateinit var isAuthenticatedUseCase: IsAuthenticatedUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        isAuthenticatedUseCase = IsAuthenticatedUseCaseImpl(
            authRepository = authRepository
        )
    }

    @Test
    fun `When user is authenticated, expect to receive 'true'`() {
        /* Arrange */
        val expectedResult: Flow<Boolean> = flowOf(value = true)

        coEvery { authRepository.isAuthenticated() } returns expectedResult

        /* Act */
        runTest {
            val result = isAuthenticatedUseCase()
            /* Assert */
            assertEquals(expectedResult, result)
        }
    }

    @Test
    fun `When user isn't authenticated, expect to receive 'null'`() {
        /* Arrange */
        val expectedResult: Flow<Boolean?> = flowOf(value = null)

        coEvery { authRepository.isAuthenticated() } returns expectedResult

        /* Act */
        runTest {
            val result = isAuthenticatedUseCase()
            /* Assert */
            assertEquals(expectedResult, result)
        }
    }
}
