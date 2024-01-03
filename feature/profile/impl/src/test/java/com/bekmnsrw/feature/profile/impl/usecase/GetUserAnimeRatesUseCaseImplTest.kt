package com.bekmnsrw.feature.profile.impl.usecase

import com.bekmnsrw.feature.profile.api.model.AnimeRates
import com.bekmnsrw.feature.profile.api.repository.ProfileRepository
import com.bekmnsrw.feature.profile.api.usecase.GetUserAnimeRatesUseCase
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

internal class GetUserAnimeRatesUseCaseImplTest {

    @MockK
    lateinit var profileRepository: ProfileRepository

    private lateinit var getUserAnimeRatesUseCase: GetUserAnimeRatesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getUserAnimeRatesUseCase = GetUserAnimeRatesUseCaseImpl(
            profileRepository = profileRepository
        )
    }

    @Test
    fun `When invoked, expect to receive list of AnimeRates`() {
        /* Arrange */
        val requestId = 1

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
            profileRepository.getUserAnimeRates(
                id = requestId
            )
        } returns expectedResult

        /* Act */
        runTest {
            val result = getUserAnimeRatesUseCase(
                id = requestId
            )
            /* Assert */
            assertEquals(expectedResult, result)
        }
    }

    @Test
    fun `When no Internet-connection, expect UnknownHostException being threw`() {
        /* Arrange */
        val requestId = 1

        coEvery {
            profileRepository.getUserAnimeRates(
                id = requestId
            )
        } throws UnknownHostException()

        /* Act */
        runTest {
            /* Assert */
            assertFailsWith<UnknownHostException> {
                getUserAnimeRatesUseCase(
                    id = requestId
                )
            }
        }
    }
}
