package xyz.junerver.redux_kotlin.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import xyz.junerver.redux_kotlin.annotation.RegisterReducer

/**
 * Description:
 * @author Junerver
 * date: 2022/8/16-9:28
 * Email: junerver@gmail.com
 * Version: v1.0
 */
class ReducerProcessor(environment: SymbolProcessorEnvironment) : SymbolProcessor {
    private val codeGenerator = environment.codeGenerator
    private val logger = environment.logger
    // 避免二次执行
    var hadRun = false
    // 避免在不需要创建文件的模块创建文件
    var isNeed = false
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(RegisterReducer::class.qualifiedName!!)
            .filter { it.validate() }

        val fileBuilder = FileSpec.builder("", "ReduxKotlinRoot")
        val classBuilder = TypeSpec.classBuilder("AppState").addModifiers(KModifier.DATA)
        val ctorBuilder = FunSpec.constructorBuilder()


        symbols
            .filter {
                // 过滤注解对象必须是函数
                it is KSFunctionDeclaration && it.validate()
            }
            .map {
                //转型
                it as KSFunctionDeclaration
            }
            .filter {
                // reducer 函数的返回值一定是与参数一相同的
                it.returnType.toString() == it.parameters[0].type.toString()
            }
            .toList()
            .forEach {
                // 拿到被注解目标的注解一个队列
                // 拿到我们自定义的注解获取到注解的参数，由于只有一个参数直接拿0
                val property = it.annotations.filter { ann ->
                    ann.shortName.asString() == RegisterReducer::class.simpleName
                }.last().arguments[0]
                // 拿到注解申明的字段名称
                val sliceStateName = property.value.toString()
                // 从函数中获取参数1，然后获得其类型
                val sliceStateType: KSType = it.parameters[0].type.resolve()
                logger.warn("注解参数=========== ${property.name!!.asString()} : $sliceStateName ")
                logger.warn("函数参数类型 ====== $sliceStateType")

                isNeed = true
                // 构造函数添加参数
                ctorBuilder.addParameter(sliceStateName, sliceStateType.toTypeName())
                // 这段代码会为构造函数增加val
                classBuilder.addProperty(
                    PropertySpec.builder(sliceStateName, sliceStateType.toTypeName())
                        .initializer(sliceStateName)
                        .build()
                )
            }
        if (!hadRun && isNeed) {
            // 遍历参数属性完毕
            classBuilder.primaryConstructor(ctorBuilder.build())
            fileBuilder.addType(classBuilder.build())
            fileBuilder.build().writeTo(codeGenerator, false)
            hadRun = true
        }
        return symbols.toList()
    }
}