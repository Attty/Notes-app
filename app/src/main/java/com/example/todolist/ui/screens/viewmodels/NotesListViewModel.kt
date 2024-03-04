package com.example.todolist.ui.screens.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.App
import com.example.todolist.entities.Note
import com.example.todolist.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotesListViewModel(private val repository: NoteRepository = App.instance.repository) :
    ViewModel() {

    val notes = repository.getAllNotes()
    val categories = repository.getAllCategories()
    val notesByCategory: List<Note> = emptyList()
    private val _selectedListStateFlow = MutableStateFlow<MutableList<Note>>(mutableListOf())
    val selectedListStateFlow: StateFlow<MutableList<Note>>
        get() = _selectedListStateFlow

    fun getNotesByCategory(categoryName: String) = viewModelScope.launch() {
       repository.getNotesByCategory(categoryName)
    }

    fun updateSelectedList(selectedList: MutableList<Note>) {
        _selectedListStateFlow.value = selectedList
    }

    fun onCheckedBoxTrueClick(note: Note) {
        val newList = _selectedListStateFlow.value.toMutableList()
        newList.add(note)
        updateSelectedList(newList)
    }

    fun onCheckedBoxFalseClick(note: Note) {
        val newList = _selectedListStateFlow.value.toMutableList()
        newList.remove(note)
        updateSelectedList(newList)
    }

    fun clearList() {
        val newList = _selectedListStateFlow.value.toMutableList()
        newList.clear()
        updateSelectedList(newList)
    }

    fun deleteListSelectedNotes() = viewModelScope.launch {
        val newList = _selectedListStateFlow.value.toMutableList()
        newList.forEach {
            repository.deleteNote(it)
        }
        newList.clear()
        updateSelectedList(newList)
    }
}