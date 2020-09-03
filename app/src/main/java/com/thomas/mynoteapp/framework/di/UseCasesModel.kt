package com.thomas.mynoteapp.framework.di

import com.thomas.core.repository.NoteRepository
import com.thomas.core.usecase.*
import com.thomas.mynoteapp.framework.UseCases
import dagger.Module
import dagger.Provides

@Module
class UseCasesModel {
    @Provides
    fun getUseCase(repository: NoteRepository) = UseCases(
        AddNote(repository),
        GetAllNotes(repository),
        GetNote(repository),
        RemoveNote(repository),
        GetWordCount()
    )
}