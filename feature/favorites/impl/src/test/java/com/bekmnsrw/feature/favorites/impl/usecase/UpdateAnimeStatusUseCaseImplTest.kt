package com.bekmnsrw.feature.favorites.impl.usecase

import com.bekmnsrw.feature.favorites.api.repository.FavoritesRepository
import com.bekmnsrw.feature.favorites.api.usecase.UpdateAnimeStatusUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException
import kotlin.test.assertFailsWith

internal class UpdateAnimeStatusUseCaseImplTest {

    @MockK
    lateinit var favoritesRepository: FavoritesRepository

    private lateinit var updateAnimeStatusUseCase: UpdateAnimeStatusUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        updateAnimeStatusUseCase = UpdateAnimeStatusUseCaseImpl(
            favoritesRepository = favoritesRepository
        )
    }

    @Test
    fun `When invoked, expect to receive string of updated status`() {
        /* Arrange */
        val requestId = 1
        val requestStatus = "watching"

        val expectedResult: Flow<String> = flowOf(value = "watching")

        coEvery {
            favoritesRepository.updateAnimeStatus(
                id = requestId,
                status = requestStatus
            )
        } returns expectedResult

        /* Act */
        runTest {
            val result = updateAnimeStatusUseCase(
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
            favoritesRepository.updateAnimeStatus(
                id = requestId,
                status = requestStatus
            )
        } throws UnknownHostException()

        /* Act */
        runTest {
            /* Assert */
            assertFailsWith<UnknownHostException> {
                updateAnimeStatusUseCase(
                    id = requestId,
                    status = requestStatus
                )
            }
        }
    }
}
