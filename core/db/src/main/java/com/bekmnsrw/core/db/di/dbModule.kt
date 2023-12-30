package com.bekmnsrw.core.db.di

import android.content.Context
import androidx.room.Room
import com.bekmnsrw.core.db.AppDatabase
import com.bekmnsrw.core.db.dao.SearchRequestDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

private const val DB_NAME = "AniLibDb"

val dbModule = module {
    single<AppDatabase> {
        provideAppDatabase(context = androidApplication().applicationContext)
    }

    single<SearchRequestDao> {
        provideSearchRequestDao(appDatabase = get())
    }
}

private fun provideAppDatabase(
    context: Context
): AppDatabase = Room.databaseBuilder(
    context = context,
    klass = AppDatabase::class.java,
    name = DB_NAME
).build()

private fun provideSearchRequestDao(
    appDatabase: AppDatabase
): SearchRequestDao = appDatabase.searchRequestDao()
