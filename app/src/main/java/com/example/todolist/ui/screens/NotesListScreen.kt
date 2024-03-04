package com.example.todolist.ui.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todolist.entities.Note
import com.example.todolist.ui.screens.viewmodels.NotesListViewModel

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesList(navController: NavController, viewModel: NotesListViewModel) {
    var dialogState by remember {
        mutableStateOf(false)
    }
    var expanded by remember { mutableStateOf(false) }
    var isEditing by remember {
        mutableStateOf(false)
    }
    val selectedList by viewModel.selectedListStateFlow.collectAsState()
    val notes by viewModel.notes.collectAsState(initial = emptyList())
    val categories by viewModel.categories.collectAsState(initial = emptyList())
    if (selectedList.isEmpty()) isEditing = false
    if (dialogState) {
        AlertDialog(
            onDismissRequest = { dialogState = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteListSelectedNotes()
                    dialogState = false
                }) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { dialogState = false }) {
                    Text(text = "Dismiss")
                }
            },
            title = { Text("Are you sure you want to delete this notes?") },
            text = { Text("This items will be deleted immediately. You can't undo this action") },
            icon = { Icons.Filled.Info }
        )
    }
    BackHandler {
        if (isEditing) {
            viewModel.clearList()
            isEditing = false
        }
    }
    Scaffold(
        topBar = {
            TopBar(
                title = if (isEditing) {
                    if (selectedList.isEmpty()) {
                        "Edit"
                    } else {
                        "Selected ${selectedList.size} elements"
                    }
                } else "categoryName",
                onSearchClick = {},
                isEditing,
                onNavIconClick = {
                    viewModel.clearList()
                    isEditing = false
                },
                onDropDownMenuClick = {
                    expanded = !expanded
                },
                expanded = expanded,
                closeDropDownMenu = {
                    expanded = false
                },
                onDeleteNoteClick = {
                    dialogState = true
                },
                onMoveToClick = {
                    navController.navigate("move_to")
                }
            )
        },
        floatingActionButton = {
            FloatingButton(
                onClick = { navController.navigate("add_note") },
                isEditing
            )
        },
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(categories) { index, item ->
                    CategoryCard(name = item.categoryName, onCategoryClick = {  })
                }
            }
            NotesColumn(
                notes, navController, isEditing,
                onNoteLongClick = {
                    isEditing = true
                }, viewModel
            )

        }
    }
}

@Composable
fun CategoryCard(
    name: String,
    onCategoryClick: () -> Unit
) {
    Card(modifier = Modifier
        .padding(4.dp)
        .clickable {
            onCategoryClick.invoke()
        }) {
        Text(text = name)
    }
}

@Composable
fun FloatingButton(onClick: () -> Unit, isEditing: Boolean) {
    if (!isEditing) {
        FloatingActionButton(onClick = { onClick.invoke() }, shape = CircleShape) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "NoteAdd")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    onSearchClick: () -> Unit,
    isEditing: Boolean,
    onNavIconClick: () -> Unit,
    onDropDownMenuClick: () -> Unit,
    expanded: Boolean,
    closeDropDownMenu: () -> Unit,
    onDeleteNoteClick: () -> Unit,
    onMoveToClick: () -> Unit
) {
    CenterAlignedTopAppBar(title = {
        Text(text = title)
    },
        actions = {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { closeDropDownMenu.invoke() }
            ) {
                DropdownMenuItem(text = { Text(text = "Delete Notes") }, onClick = {
                    closeDropDownMenu.invoke()
                    onDeleteNoteClick.invoke()
                })
                DropdownMenuItem(text = { Text(text = "Move To") }, onClick = {
                    closeDropDownMenu.invoke()
                    onMoveToClick.invoke()
                })
            }
            if (isEditing) {
                IconButton(onClick = { onDropDownMenuClick.invoke() }) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Search")
                }
            } else {
                IconButton(onClick = { onSearchClick.invoke() }) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                }
            }
        },
        navigationIcon = {
            if (isEditing) {
                IconButton(onClick = { onNavIconClick.invoke() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Cancel")
                }
            }
        }
    )
}

@Composable
fun NotesColumn(
    notes: List<Note>,
    navController: NavController,
    isEditing: Boolean, onNoteLongClick: () -> Unit,
    viewModel: NotesListViewModel
) {
    if (notes.isNotEmpty()) {
        LazyColumn(
            content = {
                itemsIndexed(notes) { index, item ->
                    Note(
                        title = item.noteName,
                        text = item.description,
                        date = item.creationTime,
                        onNoteClick = {
                            navController.navigate("note/${item.noteId}")
                        },
                        onNoteLongClick = {
                            onNoteLongClick.invoke()
                        },
                        isEditing,
                        onTrueClick = {
                            viewModel.onCheckedBoxTrueClick(item)
                        },
                        onFalseClick = {
                            viewModel.onCheckedBoxFalseClick(item)
                        }
                    )
                }
            },
            modifier = Modifier
                .fillMaxSize(),

            )
    } else {
        Column {
            Text(
                "There is no notes. To add note press on FAB (in right bottom)",
                modifier = Modifier.padding(8.dp),
                fontSize = 24.sp
            )

        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Note(
    title: String,
    text: String,
    date: String,
    onNoteClick: () -> Unit,
    onNoteLongClick: () -> Unit,
    isEditing: Boolean,
    onTrueClick: () -> Unit,
    onFalseClick: () -> Unit,
) {
    val checked = remember {
        mutableStateOf(false)
    }
    if (!isEditing) {
        checked.value = false
    }
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        modifier = Modifier
            .combinedClickable(
                onClick = {
                    if (!isEditing) {
                        onNoteClick.invoke()
                    } else {
                        if (checked.value) {
                            checked.value = false
                            onFalseClick.invoke()
                        } else {
                            checked.value = true
                            onTrueClick.invoke()
                        }

                    }
                },
                onLongClick = {
                    if (!isEditing) {
                        checked.value = true
                        onTrueClick.invoke()
                        onNoteLongClick.invoke()
                    }
                }
            )
            .padding(8.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(8.dp)
            ) {
                Column(verticalArrangement = Arrangement.SpaceBetween) {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 32.sp)
                    Text(text, fontSize = 16.sp)
                }
                Text(date, fontSize = 10.sp)
            }
            if (isEditing) {
                Checkbox(
                    checked = checked.value, onCheckedChange = {
                        checked.value = it
                        if (checked.value) {
                            onTrueClick.invoke()
                        } else if (!checked.value) {
                            onFalseClick.invoke()
                        }
                    }
                )
            }
        }
    }
}