package com.example.todolist.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.todolist.entities.Category
import com.example.todolist.entities.Note

data class CategoryWithNotes(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "noteId",
        associateBy = Junction(CategoryNoteCrossRef::class)
    )
    val notes: List<Note>
)