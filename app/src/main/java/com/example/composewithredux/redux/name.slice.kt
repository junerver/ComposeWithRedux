package com.example.composewithredux.redux

import xyz.junerver.redux_kotlin.annotation.SliceReducer

/**
 * Description:
 * @author Junerver
 * date: 2022/8/18-15:27
 * Email: junerver@gmail.com
 * Version: v1.0
 */

sealed interface NameAction {
    data class Rename(val name: String) : NameAction
    object ClearName : NameAction
}

@SliceReducer("name")
fun nameReducer(state: String, action: Any): String {
    return when (action) {
        is NameAction.Rename -> action.name
        is NameAction.ClearName -> ""
        else -> state
    }
}