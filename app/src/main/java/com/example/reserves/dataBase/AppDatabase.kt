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

@Database(entities = [DoctorData::class], version = 1)
@TypeConverters(DateTypeConverter::class, ListConverter::class)

abstract class AppDatabase : RoomDatabase() {
    abstract fun doctorDao(): DoctorDao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "db"
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }
}
