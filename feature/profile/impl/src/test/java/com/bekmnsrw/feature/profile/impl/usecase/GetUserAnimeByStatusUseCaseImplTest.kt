package com.bekmnsrw.feature.profile.impl.usecase

import com.bekmnsrw.feature.profile.api.model.AnimeRates
import com.bekmnsrw.feature.profile.api.repository.ProfileRepository
import com.bekmnsrw.feature.profile.api.usecase.GetUserAnimeByStatusUseCase
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

internal class GetUserAnimeByStatusUseCaseImplTest {

    @MockK
    lateinit var profileRepository: ProfileRepository

    private lateinit var getUserAnimeByStatusUseCase: GetUserAnimeByStatusUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getUserAnimeByStatusUseCase = GetUserAnimeByStatusUseCaseImpl(
            profileRepository = profileRepository
        )
    }

    @Test
    fun `When invoked, expect to receive list of AnimeRates`() {
        /* Arrange */
        val requestId = 1
        val requestStatus = "watching"

        val expectedResult: Flow<List<AnimeRates>> = flowOf(
            listOf(
                mockk {
                    every { image } returns "testImage1"
                    every { name } returns "testName1"
                    every { russian } returns "testRussian1"
                    every { updatedAt } returns "testUpdatedAt1"
                    every { score } returns 1
                    every { status } returns "testStatus1"
                    every { animeId } returns 1
                },
                mockk {
                    every { image } returns "testImage2"
                    every { name } returns "testName2"
                    every { russian } returns "testRussian2"
                    every { updatedAt } returns "testUpdatedAt2"
                    every { score } returns 2
                    every { status } returns "testStatus2"
                    every { animeId } returns 2
                }
            )
        )

        coEvery {
            profileRepository.getUserAnimeByStatus(
                id = requestId,
                status = requestStatus
            )
        } returns expectedResult

        /* Act */
        runTest {
            val result = getUserAnimeByStatusUseCase(
                id = requestId,
                status = requestStatus
            )
            /* Assert */
            assertEquals(expectedResult, result)
        }
    }

    @Test
    fun `When no Internet-connection, expect UnknownHostException being threw`() {
        /* Arrange */
        val requestId = 1
        val requestStatus = "watching"

        coEvery {
            profileRepository.getUserAnimeByStatus(
                id = requestId,
                status = requestStatus
            )
        } throws UnknownHostException()

        /* Act */
        runTest {
            /* Assert */
            assertFailsWith<UnknownHostException> {
                getUserAnimeByStatusUseCase(
                    id = requestId,
                    status = requestStatus
                )
            }
        }
    }
}
