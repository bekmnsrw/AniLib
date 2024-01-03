package com.bekmnsrw.feature.home.impl.usecase

import com.bekmnsrw.feature.home.api.model.Anime
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.GetSimilarAnimeListUseCase
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

internal class GetSimilarAnimeListUseCaseImplTest {

    @MockK
    lateinit var homeRepository: HomeRepository

    private lateinit var getSimilarAnimeListUseCase: GetSimilarAnimeListUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getSimilarAnimeListUseCase = GetSimilarAnimeListUseCaseImpl(
            homeRepository = homeRepository
        )
    }

    @Test
    fun `When invoked, expect to receive list of Anime`() {
        /* Arrange */
        val requestLimit = 2
        val requestId = 1

        val expectedResult: Flow<List<Anime>> = flowOf(
            listOf(
                mockk {
                    every { id } returns 1
                    every { name } returns "testName1"
                    every { score } returns "testScore1"
                    every { status } returns "restStatus1"
                    every { airedOn } returns "testAiredOn1"
                    every { releasedOn } returns "testReleasedOn1"
                    every { numberOfEpisodes } returns "24"
                    every { episodesAired } returns 1
                    every { kind } returns "testKind1"
                    every { image } returns mockk {
                        every { original } returns "testOriginalUrl1"
                        every { preview } returns "testPreviewUrl1"
                        every { x48 } returns "textX48Url1"
                        every { x96 } returns "textX96Url1"
                    }
                    every { russian } returns "testRussian1"
                },
                mockk {
                    every { id } returns 2
                    every { name } returns "testName2"
                    every { score } returns "testScore2"
                    every { status } returns "restStatus2"
                    every { airedOn } returns "testAiredOn2"
                    every { releasedOn } returns "testReleasedOn2"
                    every { numberOfEpisodes } returns "12"
                    every { episodesAired } returns 2
                    every { kind } returns "testKind2"
                    every { image } returns mockk {
                        every { original } returns "testOriginalUrl2"
                        every { preview } returns "testPreviewUrl2"
                        every { x48 } returns "textX48Url2"
                        every { x96 } returns "textX96Url2"
                    }
                    every { russian } returns "testRussian2"
                }
            )
        )

        coEvery {
            homeRepository.getSimilarAnimeList(
                id = requestId,
                limit = requestLimit
            )
        } returns expectedResult

        /* Act */
        runTest {
            val result = getSimilarAnimeListUseCase(
                id = requestId,
                limit = requestLimit
            )
            /* Assert */
            assertEquals(expectedResult, result)
        }
    }

    @Test
    fun `When no Internet-connection, expect UnknownHostException being threw`() {
        /* Arrange */
        val requestId = 1
        val requestLimit = 2

        coEvery {
            homeRepository.getSimilarAnimeList(
                id = requestId,
                limit = requestLimit
            )
        } throws UnknownHostException()

        /* Act */
        runTest {
            /* Assert */
            assertFailsWith<UnknownHostException> {
                getSimilarAnimeListUseCase(
                    id = requestId,
                    limit = requestLimit
                )
            }
        }
    }
}
