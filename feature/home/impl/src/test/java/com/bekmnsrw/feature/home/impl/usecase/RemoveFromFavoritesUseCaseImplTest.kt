package com.bekmnsrw.feature.home.impl.usecase

import com.bekmnsrw.feature.home.api.model.FavoritesActionResult
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.RemoveFromFavoritesUseCase
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

internal class RemoveFromFavoritesUseCaseImplTest {

    @MockK
    lateinit var homeRepository: HomeRepository

    private lateinit var removeFromFavoritesUseCase: RemoveFromFavoritesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        removeFromFavoritesUseCase = RemoveFromFavoritesUseCaseImpl(
            homeRepository = homeRepository
        )
    }

    @Test
    fun `When invoked, expect to receive FavoritesActionResult`() {
        /* Arrange */
        val requestId = 1
        val requestType = "Anime"

        val expectedResult: Flow<FavoritesActionResult> = flowOf(
            mockk {
                every { success } returns true
                every { notice } returns "notice"
            }
        )

        coEvery {
            homeRepository.removeFromFavorites(
                id = requestId,
                type = requestType
            )
        } returns expectedResult

        /* Act */
        runTest {
            val result = removeFromFavoritesUseCase(
                id = requestId,
                type = requestType
            )
            /* Assert */
            assertEquals(expectedResult, result)
        }
    }

    @Test
    fun `When no Internet-connection, expect UnknownHostException being threw`() {
        /* Arrange */
        val requestId = 1
        val requestType = "Anime"

        coEvery {
            homeRepository.removeFromFavorites(
                id = requestId,
                type = requestType
            )
        } throws UnknownHostException()

        /* Act */
        runTest {
            /* Assert */
            assertFailsWith<UnknownHostException> {
                removeFromFavoritesUseCase(
                    id = requestId,
                    type = requestType
                )
            }
        }
    }
}
