package com.bekmnsrw.feature.auth.impl.usecase.local

import com.bekmnsrw.feature.auth.api.usecase.local.GetLocalAccessTokenUseCase
import com.bekmnsrw.feature.auth.impl.data.datasource.local.AuthDataStore
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

internal class GetLocalAccessTokenUseCaseImplTest {

    @MockK
    lateinit var authDataStore: AuthDataStore

    private lateinit var getLocalAccessTokenUseCase: GetLocalAccessTokenUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getLocalAccessTokenUseCase = GetLocalAccessTokenUseCaseImpl(
            authDataStore = authDataStore
        )
    }

    @Test
    fun `When accessToken is stored locally, expect to receive accessToken`() {
        /* Arrange */
        val expectedResult: Flow<String> = flowOf(value = "accessToken")

        coEvery { authDataStore.getAccessToken() } returns expectedResult

        /* Act */
        runTest {
            val result = getLocalAccessTokenUseCase()
            /* Assert */
            assertEquals(expectedResult, result)
        }
    }

    @Test
    fun `When accessToken isn't stored locally, expect to receive 'null'`() {
        /* Arrange */
        val expectedResult: Flow<String?> = flowOf(value = null)

        coEvery { authDataStore.getAccessToken() } returns expectedResult

        /* Act */
        runTest {
            val result = getLocalAccessTokenUseCase()
            /* Assert */
            assertEquals(expectedResult, result)
        }
    }
}
