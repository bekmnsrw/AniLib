package com.bekmnsrw.feature.home.impl.usecase

import com.bekmnsrw.feature.home.api.model.AnimeDetails
import com.bekmnsrw.feature.home.api.model.AnimeImage
import com.bekmnsrw.feature.home.api.model.Genre
import com.bekmnsrw.feature.home.api.model.RatesScoresStat
import com.bekmnsrw.feature.home.api.model.RatesStatusesStat
import com.bekmnsrw.feature.home.api.model.UserRates
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.GetAnimeUseCase
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

internal class GetAnimeUseCaseImplTest {

    @MockK
    lateinit var homeRepository: HomeRepository

    private lateinit var getAnimeUseCase: GetAnimeUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getAnimeUseCase = GetAnimeUseCaseImpl(
            homeRepository = homeRepository
        )
    }

    @Test
    fun `When invoked, expect to receive AnimeDetails`() {
        /* Arrange */
        val requestId = 1

        val expectedGenreList = mockk<List<Genre>> {
            listOf<Genre>(
                mockk {
                    every { entryType } returns "testEntryType1"
                    every { id } returns 1
                    every { kind } returns "testKind1"
                    every { name } returns "testName1"
                    every { russian } returns "testRussian1"
                },
                mockk {
                    every { entryType } returns "testEntryType2"
                    every { id } returns 2
                    every { kind } returns "testKind2"
                    every { name } returns "testName2"
                    every { russian } returns "testRussian2"
                }
            )
        }

        val expectedImage = mockk<AnimeImage> {
            every { original } returns "testOriginalUrl1"
            every { preview } returns "testPreviewUrl1"
            every { x48 } returns "textX48Url1"
            every { x96 } returns "textX96Url1"
        }

        val expectedJapaneseList = mockk<List<String>> {
            listOf("testJapanese1", "testJapanese2")
        }

        val expectedRatesScoresStatList = mockk<List<RatesScoresStat>> {
            listOf<RatesScoresStat>(
                mockk {
                    every { name } returns 1
                    every { value } returns 1
                },
                mockk {
                    every { name } returns 2
                    every { value } returns 2
                }
            )
        }

        val expectedRatesStatusesStatList = mockk<List<RatesStatusesStat>> {
            listOf<RatesStatusesStat>(
                mockk {
                    every { name } returns "testName1"
                    every { value } returns 1
                },
                mockk {
                    every { name } returns "testName2"
                    every { value } returns 2
                }
            )
        }

        val expectedSynonymsList = mockk<List<String>> {
            listOf("testSynonym1", "testSynonym2")
        }

        val expectedUserRates = mockk<UserRates> {
            every { chapters } returns null
            every { createdAt } returns "createdAt"
            every { episodes } returns null
            every { id } returns 1
            every { rewatches } returns null
            every { score } returns null
            every { status } returns "watching"
            every { text } returns null
            every { textHtml } returns null
            every { updatedAt } returns "updatedAt"
            every { volumes } returns null
        }

        val expectedResult: Flow<AnimeDetails> = flowOf(
            mockk {
                every { airedOn } returns "testAiredOn"
                every { description } returns "testDescription"
                every { duration } returns 24
                every { episodes } returns 24
                every { episodesAired } returns 1
                every { favoured } returns true
                every { genres } returns expectedGenreList
                every { id } returns 1
                every { image } returns expectedImage
                every { japanese } returns expectedJapaneseList
                every { kind } returns "testKind"
                every { name } returns "testName"
                every { nextEpisodeAt } returns "testNextEpisodeAt"
                every { statusesStats } returns expectedRatesStatusesStatList
                every { scoresStats } returns expectedRatesScoresStatList
                every { releasedOn } returns "testReleasedOn"
                every { russian } returns "testRussian"
                every { score } returns "testScore"
                every { status } returns "restStatus"
                every { synonyms } returns expectedSynonymsList
                every { totalScoresStats } returns 1
                every { totalStatusesStats } returns 1
                every { rating } returns "testRating"
                every { userRates } returns expectedUserRates
            }
        )

        coEvery {
            homeRepository.getAnime(
                id = requestId
            )
        } returns expectedResult

        /* Act */
        runTest {
            val result = getAnimeUseCase(
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
            homeRepository.getAnime(
                id = requestId
            )
        } throws UnknownHostException()

        /* Act */
        runTest {
            /* Assert */
            assertFailsWith<UnknownHostException> {
                getAnimeUseCase(
                    id = requestId
                )
            }
        }
    }
}
