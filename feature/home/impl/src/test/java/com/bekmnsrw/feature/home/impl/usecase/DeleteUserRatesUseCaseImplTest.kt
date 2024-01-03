package com.bekmnsrw.feature.home.impl.usecase

import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.DeleteUserRatesUseCase
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

internal class DeleteUserRatesUseCaseImplTest {

    @MockK
    lateinit var homeRepository: HomeRepository

    private lateinit var deleteUserRatesUseCase: DeleteUserRatesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        deleteUserRatesUseCase = DeleteUserRatesUseCaseImpl(
            homeRepository = homeRepository
        )
    }

    @Test
    fun `When success, expect to receive 204 status code`() {
        /* Arrange */
        val requestId = 1

        val expectedResult: Flow<Int> = flowOf(
            value = HttpURLConnection.HTTP_NO_CONTENT
        )

        coEvery {
            homeRepository.deleteUserRates(
                id = requestId
            )
        } returns expectedResult

        /* Act */
        runTest {
            val result = deleteUserRatesUseCase(
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
            homeRepository.deleteUserRates(
                id = requestId
            )
        } throws UnknownHostException()

        /* Act */
        runTest {
            /* Assert */
            assertFailsWith<UnknownHostException> {
                deleteUserRatesUseCase(
                    id = requestId
                )
            }
        }
    }
}
