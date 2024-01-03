package com.bekmnsrw.feature.home.impl.usecase

import com.bekmnsrw.feature.home.api.model.Anime
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.GetAnimeListUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException
import kotlin.test.assertFailsWith

internal class GetAnimeListUseCaseImplTest {

    @MockK
    lateinit var homeRepository: HomeRepository

    private lateinit var getAnimeListUseCase: GetAnimeListUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getAnimeListUseCase = GetAnimeListUseCaseImpl(
            homeRepository = homeRepository
        )
    }

    @Test
    fun `When invoked, expect to receive list of Anime`() {
        /* Arrange */
        val requestLimit = 2
        val requestStatus = "ongoing"
        val requestOrder = "ranked"

        val expectedResult: List<Anime> = listOf(
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

        coEvery {
            homeRepository.getAnimeList(
                limit = requestLimit,
                status = requestStatus,
                order = requestOrder
            )
        } returns expectedResult

        /* Act */
        runTest {
            val result = getAnimeListUseCase(
                limit = requestLimit,
                status = requestStatus,
                order = requestOrder
            )
            /* Assert */
            assertEquals(expectedResult, result)
        }
    }

    @Test
    fun `When no Internet-connection, expect UnknownHostException being threw`() {
        /* Arrange */
        val requestLimit = 2
        val requestStatus = "ongoing"
        val requestOrder = "ranked"

        coEvery {
            homeRepository.getAnimeList(
                limit = requestLimit,
                status = requestStatus,
                order = requestOrder
            )
        } throws UnknownHostException()

        /* Act */
        runTest {
            /* Assert */
            assertFailsWith<UnknownHostException> {
                getAnimeListUseCase(
                    limit = requestLimit,
                    status = requestStatus,
                    order = requestOrder
                )
            }
        }
    }
}
