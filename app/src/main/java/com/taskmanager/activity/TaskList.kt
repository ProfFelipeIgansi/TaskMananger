package com.taskmanager.activity

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.taskmanager.activity.viewmodel.TaskListViewModel
import com.taskmanager.base.Constants
import com.taskmanager.base.Routes
import com.taskmanager.data.LocalTaskData

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskList(
    padding: PaddingValues,
    navController: NavHostController,
    listViewModel: TaskListViewModel,
    localTaskData: LocalTaskData
) {

    LaunchedEffect(key1 = listViewModel.tasks) { listViewModel.loadTasks() }
    val tasks by listViewModel.tasks.collectAsState()
    val showAlertDialog by listViewModel.showAlertDialog.collectAsState()
    val selectItem by listViewModel.selectItem.collectAsState()

    val transition = rememberInfiniteTransition()
    val fabColorAnimation by transition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Green,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Reverse,
            animation = tween(2000)
        ),
        label = ""
    )

    val fabBorderAnimation by transition.animateValue(
        typeConverter = Dp.VectorConverter,
        initialValue = 10.dp,
        targetValue = 15.dp,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Reverse,
            animation = tween(500)
        ),
    )

    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxWidth()
    ) {
        if (showAlertDialog) {
            AlertDialog(onDismissRequest = { listViewModel.setShowAlertDialog(false) },
                confirmButton = {
                    Button(onClick = {
                        listViewModel.deleteTask(selectItem)
                    }) {
                        Text(text = Constants.ALERTDIALOG.YES)
                    }
                },
                dismissButton = {
                    Button(onClick = { listViewModel.setShowAlertDialog(false) }) {
                        Text(text = Constants.ALERTDIALOG.NO)
                    }
                },
                text = { Text(text = Constants.ALERTDIALOG.CONFIRMAREXCLUSAO) }
            )
        }
        if (tasks.isNotEmpty()) {
            LazyColumn {
                tasks.forEach { task ->
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .combinedClickable(
                                    onClick = {
                                        localTaskData.saveID(Constants.TASK_KEY, task.id)
                                        navController.navigate(Routes.TaskEdit.route)
                                    },
                                    onLongClick = {
                                        listViewModel.setSelectItem(task)
                                        listViewModel.setShowAlertDialog(true)
                                    }
                                )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = task.title,
                                    modifier = Modifier.padding(
                                        start = 20.dp,
                                        end = 20.dp,
                                        top = 10.dp,
                                        bottom = 10.dp
                                    )
                                )

                                Text(
                                    text = task.content,
                                    modifier = Modifier.padding(
                                        start = 20.dp,
                                        end = 20.dp,
                                        top = 10.dp,
                                        bottom = 10.dp
                                    )
                                )
                            }
                        }
                    }
                }
            }

        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = Constants.SEMREGISTROMSG)
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(10.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = { navController.navigate(Routes.TaskAdd.route) },
            containerColor = fabColorAnimation,
            shape = RoundedCornerShape(fabBorderAnimation),
            contentColor = Color.Black
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
        }
    }

}