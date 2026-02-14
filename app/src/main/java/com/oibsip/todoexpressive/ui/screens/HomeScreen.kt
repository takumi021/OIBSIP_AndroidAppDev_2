package com.oibsip.todoexpressive.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oibsip.todoexpressive.data.model.TaskItem
import com.oibsip.todoexpressive.ui.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    onLogout: () -> Unit
) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    var showAddTask by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("To-Do + Notes") },
                actions = { TextButton(onClick = onLogout) { Text("Logout") } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddTask = true }) {
                Icon(Icons.Rounded.Add, contentDescription = "Add task")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(tasks, key = { it.id }) { task ->
                TaskCard(
                    task = task,
                    onToggle = { viewModel.toggleTask(task) },
                    onDelete = { viewModel.deleteTask(task.id) }
                )
            }
        }
    }

    if (showAddTask) {
        AddTaskDialog(
            onDismiss = { showAddTask = false },
            onAdd = { title, details, category ->
                viewModel.addTask(title, details, category)
                showAddTask = false
            }
        )
    }
}

@Composable
private fun TaskCard(task: TaskItem, onToggle: () -> Unit, onDelete: () -> Unit) {
    Card {
        Row(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
            Checkbox(checked = task.isCompleted, onCheckedChange = { onToggle() })
            Column(modifier = Modifier.weight(1f)) {
                Text(task.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                if (task.details.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(task.details, style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(Modifier.height(6.dp))
                AssistChip(onClick = { }, label = { Text(task.category) })
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Rounded.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
private fun AddTaskDialog(onDismiss: () -> Unit, onAdd: (String, String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Task") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add item") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = details, onValueChange = { details = it }, label = { Text("Notes") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Type: Task/Event/Note") })
            }
        },
        confirmButton = {
            TextButton(onClick = { if (title.isNotBlank()) onAdd(title, details, category) }) {
                Text("Save")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
