package com.bekmnsrw.feature.home.api.domain

interface GetHomeContentUseCase {

    suspend operator fun invoke(id: Int): HomeContent
}
