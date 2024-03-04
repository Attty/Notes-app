package com.example.todolist.repository

import com.example.todolist.NoteDao
import com.example.todolist.entities.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(private val noteDao: NoteDao){

    fun getAllNotes() = noteDao.getAllNotes()
    fun getAllCategories() = noteDao.getAllCategories()
    suspend fun getNotesByCategory(name: String) = withContext(Dispatchers.IO){
        noteDao.getNotesOfCategory(name)
    }

    suspend fun addNote(note: Note) = withContext(Dispatchers.IO){
        noteDao.insertNote(note)
    }

    suspend fun updateNote(note: Note) = withContext(Dispatchers.IO){
        noteDao.updateNote(note)
    }

    suspend fun getNoteById(id: Int) = withContext(Dispatchers.IO){
        noteDao.getNoteById(id)
    }

    suspend fun deleteNote(note: Note) = withContext(Dispatchers.IO){
        noteDao.deleteNote(note)
    }

}