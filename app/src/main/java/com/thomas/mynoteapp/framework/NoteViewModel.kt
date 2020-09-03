package com.thomas.mynoteapp.framework

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.thomas.core.data.Note
import com.thomas.mynoteapp.framework.di.ApplicationModule
import com.thomas.mynoteapp.framework.di.DaggerViewModelComponent
import com.thomas.mynoteapp.utils.isInFuture
import com.thomas.mynoteapp.utils.largerThan50Character
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)


//    private var internalTitle: String = "";
//    var title: String = ""
//        set(value) {
//            internalTitle = value
//            field = value
//        }
//        get() {
//            return internalTitle
//        }

    @Inject
    lateinit var useCases: UseCases

    init {
        DaggerViewModelComponent.builder()
            .applicationModule(ApplicationModule(getApplication()))
            .build().inject(this)
    }

    val saved = MutableLiveData<Boolean>()
    val currentNote = MutableLiveData<Note>()
    val toast = MutableLiveData<String>()

    private var _currentTitle: String = ""
    var currentTitle
        get() = _currentTitle
        set(value) {
            when {
                value.largerThan50Character() -> toast.postValue("Max length of title is 50")
                value.isEmpty() -> toast.postValue("Tittle can not be empty")
                else -> _currentTitle = value
            }
        }
    private var _currentContent: String = ""
    var currentContent
        get() = _currentContent
        set(value) {
            when {
                value.isEmpty() -> toast.postValue("Tittle can not be empty")
                else -> _currentContent = value
            }
        }
    private var _currentCreationTime: Long = 0L
    var currentCreationTime
        get() = if (currentId == 0L) System.currentTimeMillis() else _currentCreationTime
        set(value) {
            _currentCreationTime = value
        }
    private var _currentUpdateTime: Long = 0L
    var currentUpdateTime
        get() = _currentUpdateTime
        set(value) {
            _currentUpdateTime = value
        }
    private var _currentReminder: Long = 0L
    var currentReminder
        get() = _currentReminder
        set(value) {
            _currentReminder = value
        }
    private var _currentId: Long = 0L
    var currentId
        get() = _currentId
        set(value) {
            _currentId = value
        }

    fun saveNote() {
        if (isValidate(currentTitle, currentContent, currentReminder)) {
            val note = Note(
                title = currentTitle,
                content = currentContent,
                creationTime = currentCreationTime,
                updateTime = currentUpdateTime,
                reminder = currentReminder,
                id = currentId,
                wordCount = 0
            )
            saveNote(note)
        }

    }

    private fun isValidate(
        currentTitle: String,
        currentContent: String,
        currentReminder: Long
    ): Boolean {
        return when {
            currentTitle.isEmpty() -> {
                toast.postValue("Tittle can not be empty")
                false
            }
            currentTitle.largerThan50Character() -> {
                toast.postValue("Length of tittle can not be larger than 50 character!")
                false
            }
            currentContent.isEmpty() -> {
                toast.postValue("Content can not be empty")
                false
            }
            currentReminder.isInFuture() -> {
                toast.postValue("Reminder can not be in the future")
                false
            }
            else -> true

        }
    }

    private fun saveNote(note: Note) {
        coroutineScope.launch {
            useCases.addNote(note)
            saved.postValue(true)
        }
    }

    fun getNote(id: Long) {
        coroutineScope.launch {
            val note = useCases.getNote(id)

            note?.let {
                currentNote.postValue(it)

                _currentTitle = it.title
                _currentContent = it.content
                _currentCreationTime = it.creationTime
                _currentUpdateTime = it.updateTime
                _currentId = it.id
                _currentReminder = it.reminder
            }
        }
    }

    fun deleteNote(note: Note) {
        coroutineScope.launch {
            useCases.removeNote(note)
            saved.postValue(true)
        }
    }
}