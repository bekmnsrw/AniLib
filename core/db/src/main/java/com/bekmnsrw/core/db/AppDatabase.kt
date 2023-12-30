package com.bekmnsrw.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bekmnsrw.core.db.dao.SearchRequestDao
import com.bekmnsrw.core.db.entity.SearchRequestEntity

@Database(
    entities = [SearchRequestEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun searchRequestDao(): SearchRequestDao
}
