package com.example.todolist.entities.relations

import androidx.room.Entity

@Entity(primaryKeys = ["categoryId", "noteId"])
class CategoryNoteCrossRef (
    val categoryId: Long,
    val noteId: Long,
)