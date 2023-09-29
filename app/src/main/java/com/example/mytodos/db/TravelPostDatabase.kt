package com.example.mytodos.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mytodos.entity.EntityPost

@Database(entities = [EntityPost::class], version = 3)
abstract class TravelPostDatabase : RoomDatabase() {

    abstract fun travelpostDAO() : TravelPostDao

    companion object {

        @Volatile
        private var INSTANCE: TravelPostDatabase? = null

//        val migration_1_2 = object  : Migration(1,2){
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL(
//                    "CREATE TABLE IF NOT EXISTS Travel_Diary_Temp (" +
//                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
//                            "posttitle TEXT NOT NULL, " +
//                            "location TEXT NOT NULL, " +
//                            "imageUri TEXT, " +  // The imageUri column is nullable
//                            "username TEXT NOT NULL, " +
//                            "password TEXT NOT NULL)"
//                )
//
//                // Copy data from the old table to the temporary table
//                database.execSQL("INSERT INTO Travel_Diary_Temp (id, posttitle, location, imageUri, username, password) " +
//                        "SELECT id, posttitle, location, '', username, password FROM Travel_Diary")
//
//                // Drop the old table
//                database.execSQL("DROP TABLE Travel_Diary")
//
//                // Rename the temporary table to the original table name
//                database.execSQL("ALTER TABLE Travel_Diary_Temp RENAME TO Travel_Diary")
//
//
//
//            }
//
//        }




        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Perform the migration to remove the `imageUri` column
                database.execSQL("CREATE TABLE IF NOT EXISTS Travel_Diary_New (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "posttitle TEXT NOT NULL, " +
                        "location TEXT NOT NULL, " +
                        "username TEXT NOT NULL, " +
                        "password TEXT NOT NULL)")

                database.execSQL("INSERT INTO Travel_Diary_New (id, posttitle, location, username, password) " +
                        "SELECT id, posttitle, location, username, password FROM Travel_Diary")

                database.execSQL("DROP TABLE Travel_Diary")
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
                    ).addMigrations(MIGRATION_2_3)
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}