package com.example.todolist.ui.screens.viewmodels

import androidx.lifecycle.ViewModel
import com.example.todolist.App
import com.example.todolist.repository.NoteRepository

class MoveToViewModel(private val repository: NoteRepository = App.instance.repository) :
    ViewModel() {
}