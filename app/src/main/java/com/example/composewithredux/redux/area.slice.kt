package com.example.composewithredux.redux

import org.reduxkotlin.reducerForActionType
import xyz.junerver.redux_kotlin.annotation.SliceReducer

/**
 * Description:
 * @author Junerver
 * date: 2022/8/11-11:20
 * Email: junerver@gmail.com
 * Version: v1.0
 */

data class Area(
    val id: String,
    val name: String,
)

sealed interface AreaAction {
    data class AddArea(val area: Area) : AreaAction
    data class DelArea(val id: String) : AreaAction
}


@SliceReducer("areas")
val areaReducer = reducerForActionType<List<Area>, AreaAction> { state, action ->
    when (action) {
        is AreaAction.AddArea -> {
            state + action.area
        }
        is AreaAction.DelArea -> {
            state.filter {
                it.id != action.id
            }
        }
    }
}