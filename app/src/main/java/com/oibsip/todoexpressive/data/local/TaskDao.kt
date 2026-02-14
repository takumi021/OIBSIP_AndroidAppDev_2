package com.oibsip.todoexpressive.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.oibsip.todoexpressive.data.model.TaskItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(taskItem: TaskItem)

    @Update
    suspend fun update(taskItem: TaskItem)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun delete(taskId: Long)

    @Query("SELECT * FROM tasks WHERE ownerId = :ownerId ORDER BY id DESC")
    fun getTasksByOwner(ownerId: Long): Flow<List<TaskItem>>
}
