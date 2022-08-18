package com.example.composewithredux

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composewithredux.redux.*
import xyz.junerver.compose_redux.StoreProvider
import xyz.junerver.compose_redux.rememberDispatcher
import xyz.junerver.compose_redux.selectState
import com.example.composewithredux.ui.theme.ComposeWithReduxTheme
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeWithReduxTheme {
                // A surface container using the 'background' color from the theme
                StoreProvider(store = store) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        Column {
                            ShowName()
                            ChangeName()
                            AreaList()
                            Counter()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowName() {
    val name by selectState<AppState2, String?> { name }
    Text(text = "show redux: $name!")
}

@Composable
fun ChangeName() {
    var input by remember {
        mutableStateOf("")
    }
    val dispatch = rememberDispatcher()
    Column {
        OutlinedTextField(value = input, onValueChange = {
            input = it
        })
        Button(onClick = {
            dispatch(NameAction.Rename(input))
        }) {
            Text(text = "save")
        }
    }
}

@Composable
fun AreaList() {
    val areas by selectState<AppState2, List<Area>> { areas }
    var input by remember {
        mutableStateOf("")
    }
    val dispatch = rememberDispatcher()
    Column {
        if (areas.isNotEmpty()) {
            for (area in areas) {
                Text(text = area.name)
            }
        }
        OutlinedTextField(value = input, onValueChange = {
            input = it
        })
        Row {
            Button(onClick = {
                dispatch(Action.AddArea(Area(UUID.randomUUID().toString(), input)))
            }) {
                Text(text = "add")
            }
            if (areas.isNotEmpty()) {
                Button(
                    onClick = {
                        dispatch(Action.DelArea(areas.first().id))
                    },
                    modifier = Modifier.padding(start = 20.dp)
                ) {
                    Text(text = "del")
                }
            }
        }
    }
}

@Composable
fun Counter() {
    val areas by selectState<AppState2, List<Area>> { areas }
    Text(text = "areas : ${areas.size}")
}

