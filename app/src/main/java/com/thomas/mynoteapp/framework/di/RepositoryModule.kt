package com.thomas.mynoteapp.framework.di

import android.app.Application
import com.thomas.core.repository.NoteRepository
import com.thomas.mynoteapp.framework.RoomNoteDataSource
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun provideRepository(app: Application) = NoteRepository(RoomNoteDataSource(app))
}