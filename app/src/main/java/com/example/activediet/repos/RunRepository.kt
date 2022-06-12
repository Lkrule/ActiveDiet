package com.example.activediet.repos

import com.example.activediet.data.Run
import com.example.activediet.db.RunDAO
import javax.inject.Inject

class RunRepository @Inject constructor(
    private val runDao: RunDAO
) {
    suspend fun insertRun(run: Run) = runDao.insertRun(run)

    suspend fun deleteRun(run: Run) = runDao.deleteRun(run)

    fun allRunsSortedBy(column : String) = runDao.filterBy(column)

    fun getTotalAvgSpeed() = runDao.getTotalAvgSpeed()

    fun getTotalDist() = runDao.getTotalDistance()

    fun getTotalCalsBurned() = runDao.getTotalCalsBurned()

    fun getTotalTimeInMs() = runDao.getTotalTimeInMs()
}