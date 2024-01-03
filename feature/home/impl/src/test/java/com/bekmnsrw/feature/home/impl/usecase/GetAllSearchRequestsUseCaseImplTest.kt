package com.bekmnsrw.feature.home.impl.usecase

import com.bekmnsrw.feature.home.api.model.SearchRequest
import com.bekmnsrw.feature.home.api.repository.HomeRepository
import com.bekmnsrw.feature.home.api.usecase.GetAllSearchRequestsUseCase
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

internal class GetAllSearchRequestsUseCaseImplTest {

    @MockK
    lateinit var homeRepository: HomeRepository

    private lateinit var getAllSearchRequestsUseCase: GetAllSearchRequestsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getAllSearchRequestsUseCase = GetAllSearchRequestsUseCaseImpl(
            homeRepository = homeRepository
        )
    }

    @Test
    fun `When search history is not empty, expect to receive list of SearchRequest`() {
        /* Arrange */
        val expectedResult: Flow<List<SearchRequest>> = flowOf(
            listOf(
                mockk {
                    every { id } returns 1
                    every { query } returns "testQuery1"
                },
                mockk {
                    every { id } returns 2
                    every { query } returns "testQuery2"
                },
                mockk {
                    every { id } returns 3
                    every { query } returns "testQuery3"
                }
            )
        )

        coEvery {
            homeRepository.getAllSearchRequests()
        } returns expectedResult

        /* Act */
        runTest {
            val result = getAllSearchRequestsUseCase()
            /* Assert */
            assertEquals(expectedResult, result)
        }
    }

    @Test
    fun `When search history is empty, expect to receive empty list`() {
        /* Arrange */
        val expectedResult: Flow<List<SearchRequest>> = flowOf(
            listOf()
        )

        coEvery {
            homeRepository.getAllSearchRequests()
        } returns expectedResult

        /* Act */
        runTest {
            val result = getAllSearchRequestsUseCase()
            /* Assert */
            assertEquals(expectedResult, result)
        }
    }
}
