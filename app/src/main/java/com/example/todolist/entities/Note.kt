package com.example.todolist.entities


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val noteId: Long = 0L,
    var noteName: String,
    var description: String,
    val creationTime: String
)