package com.bekmnsrw.feature.auth.impl.usecase.local

import com.bekmnsrw.feature.auth.api.usecase.local.GetLocalRefreshTokenUseCase
import com.bekmnsrw.feature.auth.impl.data.datasource.local.AuthDataStore
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class GetLocalRefreshTokenUseCaseImplTest {

    @MockK
    lateinit var authDataStore: AuthDataStore

    private lateinit var getLocalRefreshTokenUseCase: GetLocalRefreshTokenUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getLocalRefreshTokenUseCase = GetLocalRefreshTokenUseCaseImpl(
            authDataStore = authDataStore
        )
    }

    @Test
    fun `When refreshToken is stored locally, expect to receive refreshToken`() {
        /* Arrange */
        val expectedResult: Flow<String> = flowOf(value = "refreshToken")

        coEvery { authDataStore.getRefreshToken() } returns expectedResult

        /* Act */
        runTest {
            val result = getLocalRefreshTokenUseCase()
            /* Assert */
            assertEquals(expectedResult, result)
        }
    }

    @Test
    fun `When refreshToken isn't stored locally, expect to receive 'null'`() {
        /* Arrange */
        val expectedResult: Flow<String?> = flowOf(value = null)

        coEvery { authDataStore.getRefreshToken() } returns expectedResult

        /* Act */
        runTest {
            val result = getLocalRefreshTokenUseCase()
            /* Assert */
            assertEquals(expectedResult, result)
        }
    }
}
