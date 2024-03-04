package com.example.todolist

import android.app.Application
import com.example.todolist.repository.NoteRepository

class App: Application() {

    private val database by lazy {
        NoteDatabase.getInstance(this)
    }

    val repository by lazy {
        NoteRepository(database.noteDao)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


    companion object{
        lateinit var instance: App
            private set
    }
}