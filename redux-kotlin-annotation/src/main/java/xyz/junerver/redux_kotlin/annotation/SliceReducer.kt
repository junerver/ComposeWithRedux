package xyz.junerver.redux_kotlin.annotation

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.BINARY)
annotation class SliceReducer(val name: String)