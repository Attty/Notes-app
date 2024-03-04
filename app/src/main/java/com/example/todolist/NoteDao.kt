package com.example.todolist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.todolist.entities.Category
import com.example.todolist.entities.Note
import com.example.todolist.entities.relations.CategoryNoteCrossRef
import com.example.todolist.entities.relations.CategoryWithNotes
import com.example.todolist.entities.relations.NoteWithCategories
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: Note)

    @Query("SELECT * FROM Note")
    fun getAllNotes(): Flow<List<Note>>

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM Note WHERE noteId = :noteId")
    suspend fun getNoteById(noteId: Int): Note

    @Query("SELECT * FROM Category")
    fun getAllCategories(): Flow<List<Category>>

    @Update
    suspend fun updateNote(note: Note)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryNoteCrossRef(crossRef: CategoryNoteCrossRef)

    @Transaction
    @Query("SELECT * FROM category WHERE categoryName = :categoryName")
    suspend fun getNotesOfCategory(categoryName: String): List<CategoryWithNotes>

    @Transaction
    @Query("SELECT * FROM note WHERE noteName = :noteName")
    suspend fun getCategoriesOfNote(noteName: String): List<NoteWithCategories>
}