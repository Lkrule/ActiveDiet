package com.example.activediet.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.activediet.data.Run

@Dao
interface RunDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)


    @Query("SELECT SUM(TimeInMs) FROM running_table")
    fun getTotalTimeInMs(): LiveData<Long>

    @Query("SELECT SUM(CalBurned) FROM running_table")
    fun getTotalCalsBurned(): LiveData<Int>

    @Query("SELECT SUM(DistInMeters) FROM running_table")
    fun getTotalDistance(): LiveData<Int>

    @Query("SELECT AVG(AvgSpeedInKmh) FROM running_table")
    fun getTotalAvgSpeed(): LiveData<Float>


    @Query("SELECT * FROM running_table ORDER BY " +
            "CASE WHEN :column = 'timestamp'  THEN TimeStamp END DESC, " +
            "CASE WHEN :column = 'time_ms' THEN TimeInMs END DESC, " +
            "CASE WHEN :column = 'calories' THEN CalBurned END DESC, " +
            "CASE WHEN :column = 'speed'  THEN AvgSpeedInKmh END DESC, " +
            "CASE WHEN :column = 'distance' THEN DistInMeters END DESC ")
    fun filterBy(column : String) : LiveData<List<Run>>

}