package com.taskmanager.activity.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.taskmanager.data.TaskDatabase
import com.taskmanager.data.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TaskListViewModel(
    private val localDB: TaskDatabase,
    private val auth: FirebaseAuth,
    private val cloudDB: FirebaseFirestore
) : ViewModel() {

    private var _tasks = MutableStateFlow<List<TaskEntity>>(emptyList())
    val tasks: StateFlow<List<TaskEntity>> = _tasks

    private var _showAlertDialog = MutableStateFlow(false)
    val showAlertDialog: StateFlow<Boolean> = _showAlertDialog

    private var _selectItem = MutableStateFlow(TaskEntity())
    val selectItem: StateFlow<TaskEntity> = _selectItem

    fun setSelectItem(value: TaskEntity) {
        _selectItem.value = value
    }

    private val uuid = auth.currentUser?.uid


    fun loadTasks() {
        viewModelScope.launch {
            _tasks.value = localDB.taskdao().getAll(uuid)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val querySnapshot =
                    cloudDB.collection("tasks").whereEqualTo("uuid", uuid).get().await()
                val taskID = querySnapshot.documents.first().id

                if (taskID.isNotEmpty()) cloudDB.collection("tasks").document(taskID).delete()

                localDB.taskdao().delete(task)
                _tasks.value = localDB.taskdao().getAll(uuid)

                viewModelScope.launch(Dispatchers.Main) {
                    _showAlertDialog.value = false
                }
            } catch (e: Exception) {
                Log.e(
                    "deleteTask",
                    "deleteTask: Ocorreu um erro ao tentar deletar a task \n\n Erro: $e",
                )
            }

        }

    }

    fun setShowAlertDialog(value: Boolean) {
        _showAlertDialog.value = value
    }
}