package com.example.databindingpractise.bl

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.databindingpractise.model.*

@Database(version = 10, entities = [Genre::class, Film::class])
abstract class ContactsDatabase: RoomDatabase() {

    companion object {
        private const val DB_NAME = "contactsDataBase.sqlite"
        private lateinit var instance: ContactsDatabase
        private var instanceBeenCreated = false
        private val lock = Object()

        fun newInstance(context: Context): ContactsDatabase {
            synchronized(lock) {

                instance = if (!instanceBeenCreated) {
                    Room.databaseBuilder(
                            context.applicationContext,
                            ContactsDatabase::class.java,
                            DB_NAME
                    ).fallbackToDestructiveMigration()
                            .build()
                } else {
                    instance
                }

                instanceBeenCreated = true
                return instance

            }
        }
    }

    abstract val genreDao: GenreDao
    abstract val filmDao: FilmDao
}