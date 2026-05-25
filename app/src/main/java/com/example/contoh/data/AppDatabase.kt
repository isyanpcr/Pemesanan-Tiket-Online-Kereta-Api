package com.example.contoh.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.contoh.data.dao.NoteDao
import com.example.contoh.data.entity.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ticket_database"
                )   .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance

            }
        }
    }
}
