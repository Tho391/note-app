package com.thomas.mynoteapp.framework.di

import com.thomas.mynoteapp.framework.ListViewModel
import com.thomas.mynoteapp.framework.NoteViewModel
import dagger.Component

@Component(modules = [ApplicationModule::class,RepositoryModule::class,UseCasesModel::class])
interface ViewModelComponent {
    fun inject(noteViewModel: NoteViewModel)
    fun inject(listViewModel: ListViewModel)
}