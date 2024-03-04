package com.example.todolist.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.todolist.entities.Category
import com.example.todolist.entities.Note

data class NoteWithCategories(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "noteId",
        entityColumn = "categoryId",
        associateBy = Junction(CategoryNoteCrossRef::class)
    )
    val categories: List<Category>
)