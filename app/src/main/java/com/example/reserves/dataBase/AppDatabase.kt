package com.example.reserves.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.reserves.converters.DateTypeConverter
import com.example.reserves.converters.ListConverter
import com.example.reserves.daos.DoctorDao
import com.example.reserves.entities.DoctorData
import kotlinx.coroutines.CoroutineScope

@Database(entities = [DoctorData::class], version = 1)
// @TypeConverters(DateTypeConverter::class, ListConverter::class)

abstract class AppDatabase : RoomDatabase() {
    abstract fun doctorDao(): DoctorDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "personas"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
