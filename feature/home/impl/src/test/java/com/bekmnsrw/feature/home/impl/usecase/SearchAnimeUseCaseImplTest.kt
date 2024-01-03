package com.bekmnsrw.feature.home.impl.usecase

import androidx.paging.PagingData
import com.bekmnsrw.feature.home.api.model.Anime
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.SearchAnimeUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class SearchAnimeUseCaseImplTest {

    @MockK
    lateinit var homeRepository: HomeRepository

    private lateinit var searchAnimeUseCase: SearchAnimeUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        searchAnimeUseCase = SearchAnimeUseCaseImpl(
            homeRepository = homeRepository
        )
    }

    @Test
    fun `When invoked, expect paging data of Anime`() {
        /* Arrange */
        val requestQuery = "testQuery"
        val requestStatus = null

        val expectedAnimeList = mockk<List<Anime>> {
            listOf<Anime>(
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
        }

        val expectedResult = flowOf(
            PagingData.from(
                expectedAnimeList
            )
        )

        coEvery {
            homeRepository.searchAnime(
                query = requestQuery,
                status = requestStatus
            )
        } returns expectedResult

        /* Act */
        runTest {
            val result = searchAnimeUseCase(
                query = requestQuery,
                status = requestStatus
            )
            /* Assert */
            assertEquals(expectedResult, result)
        }
    }
}
