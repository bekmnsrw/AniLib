package com.bekmnsrw.feature.home.impl.usecase

import com.bekmnsrw.feature.home.api.model.UserRates
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.CreateUserRatesUseCase
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

internal class CreateUserRatesUseCaseImplTest {

    @MockK
    lateinit var homeRepository: HomeRepository

    private lateinit var createUserRatesUseCase: CreateUserRatesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        createUserRatesUseCase = CreateUserRatesUseCaseImpl(
            homeRepository = homeRepository
        )
    }

    @Test
    fun `When invoked, expect to receive UserRates`() {
        /* Arrange */
        val requestUserId = 1
        val requestTargetId = 1
        val requestStatus = "watching"

        val expectedResult: Flow<UserRates> = flowOf(
            mockk {
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
        )

        coEvery {
            homeRepository.createUserRates(
                userId = requestUserId,
                targetId = requestTargetId,
                status = requestStatus
            )
        } returns expectedResult

        /* Act */
        runTest {
            val result = createUserRatesUseCase(
                userId = requestUserId,
                targetId = requestTargetId,
                status = requestStatus
            )
            /* Assert */
            assertEquals(expectedResult, result)
        }
    }

    @Test
    fun `When no Internet-connection, expect UnknownHostException being threw`() {
        /* Arrange */
        val requestUserId = 1
        val requestTargetId = 1
        val requestStatus = "watching"

        coEvery {
            homeRepository.createUserRates(
                userId = requestUserId,
                targetId = requestTargetId,
                status = requestStatus
            )
        } throws UnknownHostException()

        /* Act */
        runTest {
            /* Assert */
            assertFailsWith<UnknownHostException> {
                createUserRatesUseCase(
                    userId = requestUserId,
                    targetId = requestTargetId,
                    status = requestStatus
                )
            }
        }
    }
}
