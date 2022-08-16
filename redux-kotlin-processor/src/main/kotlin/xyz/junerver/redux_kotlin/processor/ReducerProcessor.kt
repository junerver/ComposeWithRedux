package xyz.junerver.redux_kotlin.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.validate
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
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(RegisterReducer::class.qualifiedName!!)
            .filterNot { it.validate() }
        symbols.forEach {
            logger.info("===========: ${it.toString()}")
        }
        return symbols.toList()
    }
}