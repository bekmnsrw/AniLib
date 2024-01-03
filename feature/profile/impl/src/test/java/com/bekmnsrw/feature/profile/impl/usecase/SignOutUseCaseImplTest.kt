package com.bekmnsrw.feature.profile.impl.usecase

import com.bekmnsrw.feature.profile.api.repository.ProfileRepository
import com.bekmnsrw.feature.profile.api.usecase.SignOutUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection
import java.net.UnknownHostException
import kotlin.test.assertFailsWith

internal class SignOutUseCaseImplTest {

    @MockK
    lateinit var profileRepository: ProfileRepository

    private lateinit var signOutUseCase: SignOutUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        signOutUseCase = SignOutUseCaseImpl(
            profileRepository = profileRepository
        )
    }

    @Test
    fun `When success, expect to receive 200 status code`() {
        /* Arrange */
        val expectedResult: Flow<Int> = flowOf(
            value = HttpURLConnection.HTTP_OK
        )

        coEvery {
            profileRepository.signOut()
        } returns expectedResult

        /* Act */
        runTest {
            val result = signOutUseCase()
            /* Assert */
            assertEquals(expectedResult, result)
        }
    }

    @Test
    fun `When no Internet-connection, expect UnknownHostException being threw`() {
        /* Arrange */
        coEvery {
            profileRepository.signOut()
        } throws UnknownHostException()

        /* Act */
        runTest {
            /* Assert */
            assertFailsWith<UnknownHostException> {
                signOutUseCase()
            }
        }
    }
}
