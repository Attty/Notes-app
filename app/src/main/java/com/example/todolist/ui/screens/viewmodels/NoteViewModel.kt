package com.example.todolist.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.App
import com.example.todolist.entities.Note
import com.example.todolist.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository = App.instance.repository): ViewModel(){

    private val _title= MutableStateFlow("")
    val title =  _title.asStateFlow()

    var note by mutableStateOf(Note(0,"", "", ""))
        private set

    fun setTitle(title: String){
        _title.value = title
    }

    private val _text= MutableStateFlow("")
    val text =  _text.asStateFlow()

    fun setText(text: String){
        _text.value = text
    }

    fun getNote(id:Int) = viewModelScope.launch {
        note = repository.getNoteById(id)
        setTitle(note.noteName)
        setText(note.description)
    }

    fun updateNote() = viewModelScope.launch{
        note.noteName = _title.value
        note.description = _text.value
        repository.updateNote(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        repository.deleteNote(note)
    }

}
