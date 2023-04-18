package com.example.roomdemo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {
    @Insert
    suspend fun insert (employeeEntity:EmployeeEntity)

    @Update
    suspend fun update (employeeEntity: EmployeeEntity)

    @Delete
    suspend fun delete (employeeEntity: EmployeeEntity)

    @Query ("SELECT * FROM `employee-table`" )
    fun fetchAllEmployees(): Flow<List<EmployeeEntity>>   //part of coroutine auto updates the value at runtime, use methods like collect, collect latest,collectIndecx, Combine to listen to flow

    @Query ("SELECT * FROM `employee-table` where id= :id" )
    fun fetchEmployeesbyid(id :Int): Flow<EmployeeEntity>

}