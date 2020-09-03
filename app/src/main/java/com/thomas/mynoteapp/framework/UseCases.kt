package com.thomas.mynoteapp.framework

import com.thomas.core.usecase.*

data class UseCases(
    val addNote: AddNote,
    val getAllNotes: GetAllNotes,
    val getNote: GetNote,
    val removeNote: RemoveNote,
    val wordCount: GetWordCount
)