package com.bekmnsrw.feature.auth.impl.usecase.remote

import com.bekmnsrw.feature.auth.api.model.AccessToken
import com.bekmnsrw.feature.auth.api.repository.AuthRepository
import com.bekmnsrw.feature.auth.api.usecase.remote.GetRemoteAccessTokenUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException
import kotlin.test.assertFailsWith

internal class GetRemoteAccessTokenUseCaseImplTest {

    @MockK
    lateinit var authRepository: AuthRepository

    private lateinit var getRemoteAccessTokenUseCase: GetRemoteAccessTokenUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getRemoteAccessTokenUseCase = GetRemoteAccessTokenUseCaseImpl(
            authRepository = authRepository
        )
    }

    @Test
    fun `When invoked, expect to receive flowOf(AccessToken)`() {
        /* Arrange */
        val expectedResult: Flow<AccessToken> = flowOf(
            mockk {
                every { accessToken } returns "accessToken"
                every { tokenType } returns "typeAccess"
                every { expiresIn } returns 1
                every { refreshToken } returns "refreshToken"
                every { scope } returns "scopeToken"
                every { createdAt } returns 1
            }
        )

        val requestGrantType = "grantType"
        val requestClientId = "clientId"
        val requestClientSecret = "clientSecret"
        val requestAuthCode = "authCode"
        val requestRedirectUri = "redirectUri"

        coEvery {
            authRepository.getAccessToken(
                grantType = requestGrantType,
                clientId = requestClientId,
                clientSecret = requestClientSecret,
                authCode = requestAuthCode,
                redirectUri = requestRedirectUri
            )
        } returns expectedResult

        /* Act */
        runTest {
            val result = getRemoteAccessTokenUseCase(
                grantType = requestGrantType,
                clientId = requestClientId,
                clientSecret = requestClientSecret,
                authCode = requestAuthCode,
                redirectUri = requestRedirectUri
            )
            /* Assert */
            assertEquals(expectedResult, result)
        }
    }

    @Test
    fun `When no Internet-connection, expect UnknownHostException being threw`() {
        /* Arrange */
        val requestGrantType = "grantType"
        val requestClientId = "clientId"
        val requestClientSecret = "clientSecret"
        val requestAuthCode = "authCode"
        val requestRedirectUri = "redirectUri"

        coEvery {
            authRepository.getAccessToken(
                grantType = requestGrantType,
                clientId = requestClientId,
                clientSecret = requestClientSecret,
                authCode = requestAuthCode,
                redirectUri = requestRedirectUri
            )
        } throws UnknownHostException()

        /* Act */
        runTest {
            /* Assert */
            assertFailsWith<UnknownHostException> {
                getRemoteAccessTokenUseCase(
                    grantType = requestGrantType,
                    clientId = requestClientId,
                    clientSecret = requestClientSecret,
                    authCode = requestAuthCode,
                    redirectUri = requestRedirectUri
                )
            }
        }
    }
}
