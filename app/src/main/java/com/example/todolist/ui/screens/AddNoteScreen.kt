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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todolist.ui.screens.viewmodels.AddNoteViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddNoteScreen(navController: NavController, viewModel: AddNoteViewModel) {
    val title = viewModel.title.collectAsState()
    val text = viewModel.text.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsFocusedAsState()
    val context = LocalContext.current

    BackHandler {
        if (!isPressed) {
            viewModel.addNote()
            navController.popBackStack()
        } else {
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    }
    Scaffold(
        topBar = {
            TopAppBarAddNoteScreen(
                navController = navController,
                isPressed = isPressed,
                onClick = {
                    if(title.value.isNotEmpty() || text.value.isNotEmpty()){
                        viewModel.addNote()
                    }
                    keyboardController?.hide()
                    focusManager.clearFocus()
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
                Column {
                    Text("Title")
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
                }
                Column {
                    Text("Text")
                    BasicTextField(
                        value = text.value,
                        onValueChange = viewModel::setText,
                        Modifier.fillMaxSize(),
                        interactionSource = interactionSource,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarAddNoteScreen(
    navController: NavController,
    isPressed: Boolean,
    onClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                onClick.invoke()
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "ArrowBack")
            }
        },
        title = {
            Text("Add Note")
        },
        actions = {
            if (isPressed) {
                IconButton(onClick = {
                    onClick.invoke()
                    navController.popBackStack()
                }) {
                    Icon(imageVector = Icons.Filled.Check, contentDescription = "Check")
                }
            }
        }
    )

}

