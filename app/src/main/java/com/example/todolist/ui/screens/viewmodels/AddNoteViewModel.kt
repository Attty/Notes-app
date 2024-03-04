package com.example.todolist.ui.screens.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.App
import com.example.todolist.entities.Note
import com.example.todolist.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class AddNoteViewModel(private val repository: NoteRepository = App.instance.repository):ViewModel() {

    private val _title= MutableStateFlow("")
    val title =  _title.asStateFlow()

    private val _text= MutableStateFlow("")
    val text =  _text.asStateFlow()

    fun setTitle(title: String){
        _title.value = title
    }

    fun setText(text: String){
        _text.value = text
    }

    fun addNote() = viewModelScope.launch {
        val currentDate = LocalDate.now()
        repository.addNote(Note(0, _title.value, _text.value, currentDate.toString()))
        _title.value = ""
        _text.value = ""
    }
}