package com.example.activediet.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.activediet.data.Run

@Dao
interface RunDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)


    @Query("SELECT SUM(time) FROM running_table")
    fun getTotalTimeInMs(): LiveData<Long>

    @Query("SELECT SUM(cals) FROM running_table")
    fun getTotalCalsBurned(): LiveData<Int>

    @Query("SELECT SUM(dist) FROM running_table")
    fun getTotalDistance(): LiveData<Int>

    @Query("SELECT AVG(speed) FROM running_table")
    fun getTotalAvgSpeed(): LiveData<Float>


    @Query("SELECT * FROM running_table ORDER BY " +
            "CASE WHEN :column = 'time_ms' THEN time END DESC, " +
            "CASE WHEN :column = 'calories' THEN cals END DESC, " +
            "CASE WHEN :column = 'speed'  THEN speed END DESC, " +
            "CASE WHEN :column = 'distance' THEN dist END DESC "
    )
    fun filterBy(column : String) : LiveData<List<Run>>

}