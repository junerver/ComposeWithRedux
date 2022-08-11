package com.example.composewithredux.redux

import org.reduxkotlin.Reducer
import org.reduxkotlin.createThreadSafeStore
import xyz.junerver.redux_kotlin.annotation.RegisterReducer

/**
 * Description:
 * @author Junerver
 * date: 2022/8/9-15:20
 * Email: junerver@gmail.com
 * Version: v1.0
 */
data class State(
    val name: String? = null,
    val areas: List<Area> = emptyList()
)

sealed interface Action {
    data class AddArea(val area: Area) : Action
    data class DelArea(val id: String) : Action
}

sealed interface NameAction {
    data class Rename(val name: String) : NameAction
    object ClearName : NameAction
}

//val reducer: Reducer<State> = reducerForActionType<State, Action> { state, action ->
//    when (action) {
//        is Action.Rename -> state.copy(name = action.name)
//        is Action.ClearName -> state.copy(name = null)
//        is Action.AddArea -> {
//            val areas = state.areas + action.area
//            state.copy(areas = areas)
//        }
//        is Action.DelArea -> {
//            state.copy(areas = state.areas.filter {
//                it.id != action.id
//            })
//        }
//    }
//}

//@RegisterReducer(name = "name")
//val nameReducer = reducerForActionType<String?, NameAction> { _, action ->
//    when (action) {
//        is NameAction.Rename -> action.name
//        is NameAction.ClearName -> null
//    }
//}

@RegisterReducer(name = "name")
fun nameReducer(state: String?, action: Any):String? {
   return  when (action) {
        is NameAction.Rename -> action.name
        is NameAction.ClearName -> null
       else -> state
   }
}


/**
 * 用于合并各个分割的reducer函数
 */
val rootReducer: Reducer<State> = { state: State, action: Any ->
    State(
        name = nameReducer(state.name, action),
        areas = areaReducer(state.areas, action)
    )
}

@RegisterReducer(name = "reducer")
fun reducer(state: State, action: Any) = State(
    name = nameReducer(state.name, action),
    areas = areaReducer(state.areas, action)
)

val store = createThreadSafeStore(rootReducer, State())

data class Area(
    val id: String,
    val areaName: String,
)