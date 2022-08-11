package xyz.junerver.redux_kotlin.processor

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
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


    @OptIn(DelicateKotlinPoetApi::class, KotlinPoetMetadataPreview::class)
    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment?
    ): Boolean {
        val messager = processingEnv.messager
        messager.printMessage(Diagnostic.Kind.NOTE, "start: --------------")
        val fileBuilder = FileSpec.builder("", "ReduxKotlinRoot")
        val classBuilder = TypeSpec.classBuilder("AppState").addModifiers(KModifier.DATA)
        val ctorBuilder = FunSpec.constructorBuilder()

        roundEnvironment?.getElementsAnnotatedWith(RegisterReducer::class.java)
            ?.forEach {
                val annotation = it.getAnnotation(RegisterReducer::class.java) // 获取注解实例
                val name = annotation.name // 拿到注解中的 参数
                val simpleName = it.simpleName
                if (it.kind == ElementKind.METHOD && it is ExecutableElement) {
                    //如果是函数，获取函数参数
                    messager.printMessage(
                        Diagnostic.Kind.NOTE,
                        "注解对象是方法: ${(it)}"
                    )
                    // 获取函数的第一个参数（state）
                    if (it.parameters.isNotEmpty()) {
                        val parameter = it.parameters[0]
                        messager.printMessage(
                            Diagnostic.Kind.NOTE,
                            "注解函数的参数: $parameter  / ${parameter is TypeElement } ${parameter is VariableElement }"
                        )
                        val ep = parameter.asType().asTypeName()
//                        val packageName = getPackage(element).qualifiedName.toString()
//                        val typeMetadata = element.getAnnotation(Metadata::class.java)
//                        val kmClass = typeMetadata.toImmutableKmClass()
//                        val className = ClassInspectorUtil.createClassName(kmClass.name)

                        messager.printMessage(
                            Diagnostic.Kind.NOTE,
                            "注解函数的参数 Metadata: $ep  "
                        )
//                        val typeMetadata = parameter.getAnnotation(Metadata::class.java)
//                        val kmClass = typeMetadata.toKmClass()
                        val parameterType = parameter.asType()
                        messager.printMessage(
                            Diagnostic.Kind.NOTE,
                            "注解函数的参数: $parameter "
                        )
                        // 构造函数添加参数
                        ctorBuilder.addParameter(name, parameterType.asTypeName())
                        // 这段代码会为构造函数增加val
                        classBuilder.addProperty(
                            PropertySpec.builder(name, parameterType.asTypeName())
                                .initializer(name)
                                .build()
                        )
                    }
                }
            }

        if (!hadRun) {

            // 遍历参数属性完毕
            classBuilder.primaryConstructor(ctorBuilder.build())
            fileBuilder.addType(classBuilder.build())
            fileBuilder.build().writeTo(filer)
            hadRun = true
        }
        //该方法返回ture表示该注解已经被处理, 后续不会再有其他处理器处理; 返回false表示仍可被其他处理器处理.
        return true
    }
}