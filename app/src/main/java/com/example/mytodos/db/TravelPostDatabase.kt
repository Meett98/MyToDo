package com.example.mytodos.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mytodos.entity.EntityPost

@Database(entities = [EntityPost::class], version = 4)
abstract class TravelPostDatabase : RoomDatabase() {

    abstract fun travelpostDAO() : TravelPostDao

    companion object {

        @Volatile
        private var INSTANCE: TravelPostDatabase? = null





        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {


                database.execSQL("CREATE TABLE IF NOT EXISTS Travel_Diary_New (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "posttitle TEXT NOT NULL, " +
                        "location TEXT NOT NULL, " +
                        "username TEXT NOT NULL, " +
                        "password TEXT NOT NULL, " +
                        "image INTEGER NOT NULL DEFAULT 0)")

                // Copy data from the old table to the new table
                database.execSQL("INSERT INTO Travel_Diary_New (id, posttitle, location, username, password) " +
                        "SELECT id, posttitle, location, username, password FROM Travel_Diary")

                // Drop the old table
                database.execSQL("DROP TABLE Travel_Diary")

                // Rename the new table to the original table name
                database.execSQL("ALTER TABLE Travel_Diary_New RENAME TO Travel_Diary")

            }
        }

        fun getDatabase(context: Context): TravelPostDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        TravelPostDatabase::class.java,
                        "travelpost_database"
                    ).addMigrations(MIGRATION_3_4)
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}