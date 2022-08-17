package com.example.composewithredux.redux

import AppState
import org.reduxkotlin.createThreadSafeStore
import org.reduxkotlin.reducerForActionType
import rootReducer
import xyz.junerver.redux_kotlin.annotation.SliceReducer

/**
 * Description:
 * @author Junerver
 * date: 2022/8/9-15:20
 * Email: junerver@gmail.com
 * Version: v1.0
 */


sealed interface Action {
    data class AddArea(val area: Area) : Action
    data class DelArea(val id: String) : Action
}

sealed interface NameAction {
    data class Rename(val name: String) : NameAction
    object ClearName : NameAction
}


@SliceReducer(name = "other")
val otherReducer = reducerForActionType<String?, NameAction> { _, action ->
    when (action) {
        is NameAction.Rename -> action.name
        is NameAction.ClearName -> null
    }
}

@SliceReducer(name = "name")
fun nameReducer(state: String?, action: Any):String? {
   return  when (action) {
        is NameAction.Rename -> action.name
        is NameAction.ClearName -> null
       else -> state
   }
}

@SliceReducer(name = "flag")
fun flagReducer(state: Boolean, action: Any): Boolean {
    return when (action) {
        is NameAction.Rename -> true
        is NameAction.ClearName -> false
        else -> state
    }
}


val store = createThreadSafeStore(::rootReducer, AppState(emptyList(), null, false))

data class Area(
    val id: String,
    val areaName: String,
)