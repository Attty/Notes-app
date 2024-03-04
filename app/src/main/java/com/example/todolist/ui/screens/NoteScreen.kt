package com.example.todolist.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todolist.ui.screens.viewmodels.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun Note(navController: NavController, viewModel: NoteViewModel, id: Int?) {
    LaunchedEffect(Unit) {
        viewModel.getNote(id!!)
    }
    val title = viewModel.title.collectAsState()
    val text = viewModel.text.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsFocusedAsState()
    var expanded by remember { mutableStateOf(false) }
    var dialogState by remember {
        mutableStateOf(false)
    }
    if(dialogState){
        AlertDialog(
            onDismissRequest = { dialogState = false },
            confirmButton = {
                TextButton(onClick = {  viewModel.deleteNote(viewModel.note)
                    dialogState = false
                    navController.popBackStack() }) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { dialogState = false}) {
                    Text(text = "Dismiss")
                }
            },
            title = { Text("Are you sure you want to delete this note?") },
            text = { Text("This item will be deleted immediately. You can't undo this action") },
            icon = { Icons.Filled.Info }
        )
    }
    BackHandler {
        if (!isPressed) {
            navController.popBackStack()
        } else {
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    }
    Scaffold(
        topBar = {
            TopBar(navController = navController,
                isPressed = isPressed,
                onClick = {
                    viewModel.updateNote()
                    keyboardController?.hide()
                    focusManager.clearFocus()
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
                })
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.padding(8.dp)) {
                BasicTextField(
                    value = title.value,
                    onValueChange = viewModel::setTitle,
                    maxLines = 4,
                    textStyle = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    interactionSource = interactionSource,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    )
                )
                Column {
                    BasicTextField(
                        value = text.value,
                        onValueChange = viewModel::setText,
                        Modifier.fillMaxSize(),
                        interactionSource = interactionSource
                    )
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val text = "Update Note"
                        Text(text)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    isPressed: Boolean,
    onClick: () -> Unit,
    onDropDownMenuClick: () -> Unit,
    expanded: Boolean,
    closeDropDownMenu: () -> Unit,
    onDeleteNoteClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "ArrowBack")
            }
        },
        title = {
            Text("Edit Note")
        },
        actions = {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { closeDropDownMenu.invoke() }
            ) {
                DropdownMenuItem(text = { Text(text = "Delete Note") }, onClick = {
                    closeDropDownMenu.invoke()
                    onDeleteNoteClick.invoke()
                })
            }
            if (isPressed) {
                IconButton(onClick = { onClick.invoke() }) {
                    Icon(imageVector = Icons.Filled.Check, contentDescription = "Check")
                }
            } else {
                IconButton(onClick = { onDropDownMenuClick.invoke() }) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "MoreVet")
                }
            }
        }
    )
}