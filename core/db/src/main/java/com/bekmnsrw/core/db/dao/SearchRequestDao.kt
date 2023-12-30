package com.bekmnsrw.core.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bekmnsrw.core.db.entity.SearchRequestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchRequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSearchRequest(searchRequestEntity: SearchRequestEntity)

    @Query("DELETE FROM search_request WHERE id = :id")
    suspend fun deleteSearchRequestById(id: Int)

    @Query("SELECT * FROM search_request ORDER BY id DESC")
    fun getAllSearchRequestsOrderedByIdDesc(): Flow<List<SearchRequestEntity>>

    @Query("DELETE FROM search_request")
    suspend fun deleteAllSearchRequests()
}
