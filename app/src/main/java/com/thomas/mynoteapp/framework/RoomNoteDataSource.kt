package com.thomas.mynoteapp.framework

import android.content.Context
import com.thomas.core.data.Note
import com.thomas.core.repository.NoteDataSource
import com.thomas.mynoteapp.framework.db.DatabaseService
import com.thomas.mynoteapp.framework.db.NoteEntity

class RoomNoteDataSource(context: Context) : NoteDataSource {
    private val noteDao = DatabaseService.getInstance(context).noteDao()

    override suspend fun add(note: Note) = noteDao.addNoteEntity(NoteEntity.fromNote(note))

    override suspend fun get(id: Long): Note? = noteDao.getNoteEntity(id)?.toNote()

    override suspend fun getAll(): List<Note> = noteDao.getAllNoteEntity().map { it.toNote() }

    override suspend fun remove(note: Note) = noteDao.deleteNoteEntity(NoteEntity.fromNote(note))
}