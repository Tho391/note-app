package com.thomas.core.usecase

import com.thomas.core.data.Note
import com.thomas.core.repository.NoteRepository

class AddNote(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(note: Note) = noteRepository.addNote(note)
}