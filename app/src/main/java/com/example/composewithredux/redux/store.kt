package com.example.composewithredux.redux

import org.reduxkotlin.createThreadSafeStore

/**
 * Description:
 * @author Junerver
 * date: 2022/8/9-15:20
 * Email: junerver@gmail.com
 * Version: v1.0
 */


val store = createThreadSafeStore(
    ::rootReducer, AppState(
        areas = listOf(
            Area(id = "1", name = "beijing"),
            Area(id = "2", name = "shanghai"),
            Area(id = "3", name = "guangzhou")
        ),
        name = "junerver",
    )
)
