package com.example.todolist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todolist.entities.Category
import com.example.todolist.entities.Note
import com.example.todolist.entities.relations.CategoryNoteCrossRef


@Database(
    entities = [
        Category::class,
        Note::class,
        CategoryNoteCrossRef::class
    ],
    version = 1
)
abstract class NoteDatabase: RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object{
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase{
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}