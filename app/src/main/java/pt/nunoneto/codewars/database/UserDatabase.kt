package pt.nunoneto.codewars.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import pt.nunoneto.codewars.entities.User

@Database(entities = [(User::class)], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase? {
            if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, UserDatabase::class.java, "database.db")
                            .build()
            }

            return INSTANCE
        }
    }
}