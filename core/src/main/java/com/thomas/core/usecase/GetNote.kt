package com.thomas.core.usecase

import com.thomas.core.repository.NoteRepository

class GetNote(private val noteRepository: NoteRepository){
    suspend operator fun invoke(id: Long) = noteRepository.getNote(id)
}