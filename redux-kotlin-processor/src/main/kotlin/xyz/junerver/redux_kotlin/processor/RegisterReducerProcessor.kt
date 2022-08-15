package xyz.junerver.redux_kotlin.processor

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import com.squareup.kotlinpoet.metadata.specs.ClassInspector
import com.squareup.kotlinpoet.metadata.toKmClass
import xyz.junerver.redux_kotlin.annotation.RegisterReducer
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.tools.Diagnostic

// 注解处理器必须是 Java Library
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class RegisterReducerProcessor : AbstractProcessor() {
    lateinit var filer: Filer
    var hadRun = false
    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        filer = processingEnv!!.filer
    }

    //返回要处理哪些自定义注解，也可以使用 @SupportedAnnotationTypes() 它的返回值是 process() 方法的第一个参数
    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(RegisterReducer::class.java.canonicalName)
    }


    @OptIn(DelicateKotlinPoetApi::class)
    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment?
    ): Boolean {
        // file name
        val fileBuilder = FileSpec.builder("", "ReduxKotlinRoot")
        // data class
        val classBuilder = TypeSpec.classBuilder("AppState").addModifiers(KModifier.DATA)
        val ctorBuilder = FunSpec.constructorBuilder()
        roundEnvironment?.getElementsAnnotatedWith(RegisterReducer::class.java)
            ?.forEach {
                val annotation = it.getAnnotation(RegisterReducer::class.java)
                val name = annotation.name
                if (it.kind == ElementKind.METHOD && it is ExecutableElement) {
                    if (it.parameters.isNotEmpty()) {
                        val parameter = it.parameters[0] as VariableElement
                        val parameterType = parameter.asType()
                        // add first param to data class with custom name
                        ctorBuilder.addParameter(name, parameterType.asTypeName())
                        classBuilder.addProperty(
                            PropertySpec.builder(name, parameterType.asTypeName())
                                .initializer(name)
                                .build()
                        )
                    }
                }
            }
        if (!hadRun) {
            classBuilder.primaryConstructor(ctorBuilder.build())
            fileBuilder.addType(classBuilder.build())
            fileBuilder.build().writeTo(filer)
            hadRun = true
        }
        return true
    }
}

fun TypeName.javaToKotlinType(): TypeName = when (this) {
    is ParameterizedTypeName -> {
        (rawType.javaToKotlinType() as ClassName).parameterizedBy(
            *typeArguments.map {
                it.javaToKotlinType()
            }.toTypedArray()
        )
    }
    is WildcardTypeName -> {
        if (inTypes.isNotEmpty()) WildcardTypeName.consumerOf(inTypes[0].javaToKotlinType())
        else WildcardTypeName.producerOf(outTypes[0].javaToKotlinType())
    }

    else -> {
        val className = JavaToKotlinClassMap
            .mapJavaToKotlin(FqName(toString()))?.asSingleFqName()?.asString()
        if (className == null) this
        else ClassName.bestGuess(className)
    }
}