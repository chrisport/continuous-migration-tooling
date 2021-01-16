package ch.chrisport.continuousrewrite

import ch.chrisport.continuousrewrite.reporting.DiscrepancyReporter
import ch.chrisport.continuousrewrite.reporting.DiscrepancyType.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class RewriteMerger(
    private val discrepancyReporter: DiscrepancyReporter,
    private val mapper: ObjectMapper = jacksonObjectMapper()
) {
    fun merge(
        original: Any,
        rewrite: Any
    ) {
        val originalMap: Map<String, Any> = mapper.convertValue(original)
        val rewriteMap: Map<String, Any> = mapper.convertValue(rewrite)
        mergeMap(originalMap, rewriteMap)
    }

    private fun mergeMap(
        original: Map<*, *>,
        rewrite: Map<*, *>,
        parents: FieldPointer = ""
    ) {
        val keys = original.keys + rewrite.keys
        keys.forEach { fieldNameStar ->
            val fieldName = "$fieldNameStar"
            val fieldPointer = fieldPointer(fieldName, parents)
            val rewriteValue = rewrite[fieldName]
            val originalValue = original[fieldName]

            if (originalValue is Map<*, *> && rewriteValue is Map<*, *>) {
                mergeMap(originalValue, rewriteValue, fieldPointer)
            } else if (rewriteValue != originalValue) {
                reportDiscrepancy(fieldPointer, originalValue, rewriteValue)
            }
        }
    }

    private fun reportDiscrepancy(key: String, originalValue: Any?, rewriteValue: Any?) {
        val type = when {
            originalValue == null -> MISSING_IN_ORIGINAL
            rewriteValue == null -> MISSING_IN_REWRITE
            else -> VALUE_MISMATCH
        }
        discrepancyReporter.report(originalValue, rewriteValue, key, type)
    }
}
