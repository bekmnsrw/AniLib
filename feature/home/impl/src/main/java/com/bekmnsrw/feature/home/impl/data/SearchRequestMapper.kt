package com.bekmnsrw.feature.home.impl.data

import com.bekmnsrw.core.db.entity.SearchRequestEntity
import com.bekmnsrw.feature.home.api.model.SearchRequest

internal fun SearchRequest.toSearchRequestEntity(): SearchRequestEntity = SearchRequestEntity(
    id = id,
    query = query
)

internal fun SearchRequestEntity.toSearchRequest(): SearchRequest = SearchRequest(
    id = id,
    query = query
)

internal fun List<SearchRequestEntity>.toSearchRequestList(): List<SearchRequest> = this.map {
    it.toSearchRequest()
}
