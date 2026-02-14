package com.oibsip.todoexpressive.data.repo

import android.content.Context
import androidx.core.content.edit
import com.oibsip.todoexpressive.data.local.AppDatabase
import com.oibsip.todoexpressive.data.model.TaskItem
import com.oibsip.todoexpressive.data.model.User
import kotlinx.coroutines.flow.Flow

class AppRepository(context: Context) {
    private val database = AppDatabase.getInstance(context)
    private val userDao = database.userDao()
    private val taskDao = database.taskDao()
    private val sessionPrefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    suspend fun signUp(fullName: String, email: String, password: String): Result<Long> {
        val existing = userDao.findByEmail(email)
        if (existing != null) {
            return Result.failure(IllegalArgumentException("Email already registered"))
        }
        val id = userDao.insert(User(fullName = fullName, email = email, password = password))
        setSession(id)
        return Result.success(id)
    }

    suspend fun login(email: String, password: String): Result<Long> {
        val user = userDao.login(email, password)
            ?: return Result.failure(IllegalArgumentException("Invalid credentials"))
        setSession(user.id)
        return Result.success(user.id)
    }

    fun currentUserId(): Long? {
        val id = sessionPrefs.getLong("user_id", -1)
        return id.takeIf { it != -1L }
    }

    fun logout() {
        sessionPrefs.edit { remove("user_id") }
    }

    fun tasks(ownerId: Long): Flow<List<TaskItem>> = taskDao.getTasksByOwner(ownerId)

    suspend fun addTask(ownerId: Long, title: String, details: String, category: String) {
        taskDao.insert(TaskItem(ownerId = ownerId, title = title, details = details, category = category))
    }

    suspend fun toggleTask(task: TaskItem) {
        taskDao.update(task.copy(isCompleted = !task.isCompleted))
    }

    suspend fun deleteTask(taskId: Long) {
        taskDao.delete(taskId)
    }

    private fun setSession(userId: Long) {
        sessionPrefs.edit { putLong("user_id", userId) }
    }
}
