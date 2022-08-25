package com.example.composewithredux

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composewithredux.redux.*
import com.example.composewithredux.ui.theme.ComposeWithReduxTheme
import xyz.junerver.compose_redux.StoreProvider
import xyz.junerver.compose_redux.rememberDispatcher
import xyz.junerver.compose_redux.selectState
import java.util.*

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
    val name by selectState<AppState, String?> { name }
    MyText(text = name ?: "")
}

@Preview(showBackground = true)
@Composable
fun MyText(text:String = "xzxcasasdasdasddasdasdzxc"){
    Text(
        text = "show redux: $text!",
        modifier = Modifier
            .size(150.dp)
            .background(Color.Red),
        textAlign = TextAlign.Center,
        lineHeight = 30.sp,
        fontSize = 15.sp
    )
}

@Composable
fun ChangeName() {
    var input by remember {
        mutableStateOf("")
    }
    val dispatch = rememberDispatcher()
    Column {
        OutlinedTextField(value = input, onValueChange = { input = it })
        Button(onClick = { dispatch(NameAction.Rename(input)) }) {
            Text(text = "save")
        }
    }
}

@Composable
fun AreaList() {
    val areas by selectState<AppState, List<Area>> { areas }
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
                dispatch(AreaAction.AddArea(Area(UUID.randomUUID().toString(), input)))
            }) {
                Text(text = "add")
            }
            if (areas.isNotEmpty()) {
                Button(
                    onClick = {
                        dispatch(AreaAction.DelArea(areas.first().id))
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
    val areas by selectState<AppState, List<Area>> { areas }
    Text(text = "areas : ${areas.size}")
}

