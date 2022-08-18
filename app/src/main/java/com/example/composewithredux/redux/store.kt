package com.example.composewithredux.redux

import org.reduxkotlin.createThreadSafeStore
import xyz.junerver.redux_kotlin.annotation.SliceReducer

/**
 * Description:
 * @author Junerver
 * date: 2022/8/9-15:20
 * Email: junerver@gmail.com
 * Version: v1.0
 */


sealed interface NameAction {
    data class Rename(val name: String) : NameAction
    object ClearName : NameAction
}

@SliceReducer(name = "name")
fun nameReducer(state: String?, action: Any): String? {
    return when (action) {
        is NameAction.Rename -> action.name
        is NameAction.ClearName -> null
        else -> state
    }
}


val store = createThreadSafeStore(
    ::rootReducer, AppState(
        areas = listOf(
            Area(id = "1", name = "北京"),
            Area(id = "2", name = "上海"),
            Area(id = "3", name = "广州")
        ),
        name = "junerver",
    )
)
