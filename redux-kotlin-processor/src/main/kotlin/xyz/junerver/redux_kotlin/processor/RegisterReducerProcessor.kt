package xyz.junerver.redux_kotlin.processor

import com.google.auto.service.AutoService
import xyz.junerver.redux_kotlin.annotation.RegisterReducer
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

// 注解处理器必须是 Java Library
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class RegisterReducerProcessor : AbstractProcessor() {

    //返回要处理哪些自定义注解，也可以使用 @SupportedAnnotationTypes() 它的返回值是 process() 方法的第一个参数
    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(RegisterReducer::class.java.canonicalName)
    }


    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment?
    ): Boolean {
        val messager = processingEnv.messager
        messager.printMessage(Diagnostic.Kind.NOTE, "start: --------------")
        val cardList = roundEnvironment?.getElementsAnnotatedWith(RegisterReducer::class.java)
            // 这里强转成 TypeElement 是为了方便获取更多有用的信息
//            ?.map { it as TypeElement }
            ?.forEach {
                val annotation = it.getAnnotation(RegisterReducer::class.java) // 获取注解实例
                val name = annotation.name // 拿到注解中的 参数
                val kind = it.kind
                val simpleName = it.simpleName
                if (it.kind == ElementKind.METHOD && it is ExecutableElement) {
                    //如果是函数，获取函数参数
                    messager.printMessage(
                        Diagnostic.Kind.NOTE,
                        "注解对象是方法: ${(it)}"
                    )
                    // 获取函数所在类名
                    val e = it.enclosingElement
                    if (it.parameters.isNotEmpty()){
                        val parameter = it.parameters[0].asType().toString()
                        messager.printMessage(
                            Diagnostic.Kind.NOTE,
                            "注解函数的参数: ${(parameter)}"
                        )
                    }

//                    for (parameter in it.typeParameters) {
//                        messager.printMessage(Diagnostic.Kind.NOTE, "parameter: $parameter")
//                    }

                }
                messager.printMessage(
                    Diagnostic.Kind.NOTE,
                    "${simpleName}  $kind 注解字段名称 --> ${name} "
                )
//                name to carClazz // 准换成一个 Map
            }


        //该方法返回ture表示该注解已经被处理, 后续不会再有其他处理器处理; 返回false表示仍可被其他处理器处理.
        return true
    }
}