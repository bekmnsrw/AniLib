package com.bekmnsrw.feature.profile.impl.usecase

import com.bekmnsrw.feature.profile.api.model.Image
import com.bekmnsrw.feature.profile.api.model.WhoAmI
import com.bekmnsrw.feature.profile.api.repository.ProfileRepository
import com.bekmnsrw.feature.profile.api.usecase.GetProfileUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException
import kotlin.test.assertFailsWith

internal class GetProfileUseCaseImplTest {

    @MockK
    lateinit var profileRepository: ProfileRepository

    private lateinit var getProfileUseCase: GetProfileUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getProfileUseCase = GetProfileUseCaseImpl(
            profileRepository = profileRepository
        )
    }

    @Test
    fun `When invoked, expect to receive WhoAmI`() {
        /* Arrange */
        val expectedImage = mockk<Image> {
            every { x148 } returns "textX148Url"
            every { x16 } returns "textX16Url"
            every { x160 } returns "textX160Url"
            every { x32 } returns "textX32Url"
            every { x48 } returns "textX48Url"
            every { x64 } returns "textX64Url"
            every { x80 } returns "textX80Url"
        }

        val expectedResult: Flow<WhoAmI> = flowOf(
            mockk {
                every { avatar } returns "testAvatarUrl1"
                every { birthOn } returns null
                every { fullYears } returns null
                every { id } returns 1
                every { image } returns expectedImage
                every { lastOnlineAt } returns "testLastOnlineAt"
                every { locale } returns "testLocale"
                every { name } returns "testName"
                every { nickname } returns "testNickname"
                every { sex } returns null
                every { url } returns "testUrl"
                every { website } returns null
            }
        )

        coEvery {
            profileRepository.getProfile()
        } returns expectedResult

        /* Act */
        runTest {
            val result = getProfileUseCase()
            /* Assert */
            assertEquals(expectedResult, result)
        }
    }

    @Test
    fun `When no Internet-connection, expect UnknownHostException being threw`() {
        /* Arrange */
        coEvery {
            profileRepository.getProfile()
        } throws UnknownHostException()

        /* Act */
        runTest {
            /* Assert */
            assertFailsWith<UnknownHostException> {
                getProfileUseCase()
            }
        }
    }
}
