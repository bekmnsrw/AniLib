package com.bekmnsrw.feature.favorites.impl.usecase

import com.bekmnsrw.feature.favorites.api.model.FavoriteAnime
import com.bekmnsrw.feature.favorites.api.repository.FavoritesRepository
import com.bekmnsrw.feature.favorites.api.usecase.GetUserFavoritesUseCase
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

internal class GetUserFavoritesUseCaseImplTest {

    @MockK
    lateinit var favoritesRepository: FavoritesRepository

    private lateinit var getUserFavoritesUseCase: GetUserFavoritesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getUserFavoritesUseCase = GetUserFavoritesUseCaseImpl(
            favoritesRepository = favoritesRepository
        )
    }

    @Test
    fun `When invoked, expect to receive list of FavoriteAnime`() {
        /* Arrange */
        val requestId = 1

        val expectedResult: Flow<List<FavoriteAnime>> = flowOf(
            listOf(
                mockk {
                    every { id } returns 1
                    every { name } returns "testName1"
                    every { russian } returns "testRussianName1"
                    every { image } returns "testUrl1"
                    every { isFavourite } returns true
                },
                mockk {
                    every { id } returns 2
                    every { name } returns "testName2"
                    every { russian } returns "testRussianName2"
                    every { image } returns "testUrl2"
                    every { isFavourite } returns false
                },
                mockk {
                    every { id } returns 3
                    every { name } returns "testName3"
                    every { russian } returns "testRussianName3"
                    every { image } returns "testUrl3"
                    every { isFavourite } returns true
                }
            )
        )

        coEvery {
            favoritesRepository.getUserFavorites(
                id = requestId
            )
        } returns expectedResult

        /* Act */
        runTest {
            val result = getUserFavoritesUseCase(
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
            favoritesRepository.getUserFavorites(
                id = requestId
            )
        } throws UnknownHostException()

        /* Act */
        runTest {
            /* Assert */
            assertFailsWith<UnknownHostException> {
                getUserFavoritesUseCase(
                    id = requestId
                )
            }
        }
    }
}
