package com.example.todolist

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todolist.ui.screens.AddNoteScreen
import com.example.todolist.ui.screens.MoveToScreen
import com.example.todolist.ui.screens.Note
import com.example.todolist.ui.screens.NotesList
import com.example.todolist.ui.screens.viewmodels.AddNoteViewModel
import com.example.todolist.ui.screens.viewmodels.MoveToViewModel
import com.example.todolist.ui.screens.viewmodels.NoteViewModel
import com.example.todolist.ui.screens.viewmodels.NotesListViewModel

@Composable
fun NotesApp() {
    val navController = rememberNavController()
    val notesListViewModel: NotesListViewModel = viewModel()
    val noteViewModel: NoteViewModel = viewModel()
    val addNoteViewModel: AddNoteViewModel = viewModel()
    val moveToViewModel: MoveToViewModel = viewModel()


    Navigation(
        navController = navController, notesListViewModel, noteViewModel, addNoteViewModel,
        moveToViewModel
    )

}

@Composable
fun Navigation(
    navController: NavHostController,
    notesListViewModel: NotesListViewModel,
    noteViewModel: NoteViewModel,
    addNoteViewModel: AddNoteViewModel,
    moveToViewModel: MoveToViewModel
) {
    NavHost(
        navController = navController, startDestination = "notes_list",
        enterTransition = {
            fadeIn(animationSpec = tween(0))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(0))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(0))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(0))
        },
    ) {
        composable("notes_list") {
            NotesList(navController, notesListViewModel)
        }
        composable("note/{note_id}",
            arguments = listOf(
                navArgument("note_id") {
                    this.type = NavType.IntType
                }
            )
        ) {
            val id = it?.arguments?.getInt("note_id")
            Note(navController, noteViewModel, id)
        }
        composable("add_note") {
            AddNoteScreen(navController, addNoteViewModel)
        }
        composable("move_to"){
            MoveToScreen(navController, moveToViewModel)
        }
    }
}