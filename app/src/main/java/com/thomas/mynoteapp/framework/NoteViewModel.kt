package com.thomas.mynoteapp.framework

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.thomas.core.data.Note
import com.thomas.mynoteapp.framework.di.ApplicationModule
import com.thomas.mynoteapp.framework.di.DaggerViewModelComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    companion object {
        private const val MAX_LENGTH = 50
    }
    ;
    private var internalTitle: String = "";
    var title: String = ""
        set(value) {
            internalTitle = value
            field = value
        }
        get() {
            return internalTitle
        }
    private var description: String = "";
    private var dateTime: Date = Date();

    @Inject
    lateinit var useCases: UseCases

    init {
        DaggerViewModelComponent.builder()
            .applicationModule(ApplicationModule(getApplication()))
            .build().inject(this)
    }

    val saved = MutableLiveData<Boolean>()

    val currentNote = MutableLiveData<Note>()

    fun saveNote(note: Note) {
        //validate here
//        if validate = true
        val isValidate = validate(note)
        if (isValidate) {
            coroutineScope.launch {
                useCases.addNote(note)
                saved.postValue(true)
            }
        } else {

        }
    }


    private fun validate(note: Note): Boolean {
        return when {
            note.title.isEmpty() || note.title.length > MAX_LENGTH -> false
            note.content.isEmpty() -> false
            else -> true
        }
    }

    fun getNote(id: Long) {
        coroutineScope.launch {
            val note = useCases.getNote(id)
            currentNote.postValue(note)
        }
    }

    fun deleteNote(note: Note) {
        coroutineScope.launch {
            useCases.removeNote(note)
            saved.postValue(true)
        }
    }
}