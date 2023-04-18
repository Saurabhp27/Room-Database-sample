package com.example.roomdemo

import android.content.Context
import android.os.Build.VERSION
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EmployeeEntity :: class], version = 1)
abstract class EmployeeDatabase : RoomDatabase() {

    abstract fun employeeDao(): EmployeeDao

    companion object{

        @Volatile
        private var INSTANCE : EmployeeDatabase? = null

        fun getinstance (context : Context): EmployeeDatabase {

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        EmployeeDatabase::class.java,
                        "employee_database").fallbackToDestructiveMigration().build()
                INSTANCE = instance

                }
                return instance
            }
        }
    }
}