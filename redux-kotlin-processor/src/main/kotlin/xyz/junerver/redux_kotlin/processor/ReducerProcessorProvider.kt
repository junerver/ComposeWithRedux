package xyz.junerver.redux_kotlin.processor

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

/**
 * Description: KSP注解处理器的入口
 * @author Junerver
 * date: 2022/8/16-16:37
 * Email: junerver@gmail.com
 * Version: v1.0
 */

@AutoService(SymbolProcessorProvider::class)
class ReducerProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ReducerProcessor(
            environment
        )
    }
}