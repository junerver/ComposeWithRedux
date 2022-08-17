package com.example.composewithredux.redux

import org.reduxkotlin.reducerForActionType
import xyz.junerver.redux_kotlin.annotation.RegisterReducer

/**
 * Description:
 * @author Junerver
 * date: 2022/8/11-11:20
 * Email: junerver@gmail.com
 * Version: v1.0
 */
@RegisterReducer("areas")
fun areaReducer(state: List<Area>, action: Any): List<Area> {
    return when (action) {
        is Action.AddArea -> {
            state + action.area
        }
        is Action.DelArea -> {
            state.filter {
                it.id != action.id
            }
        }
        else -> state
    }
}