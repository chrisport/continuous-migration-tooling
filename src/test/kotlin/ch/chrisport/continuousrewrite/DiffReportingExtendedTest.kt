package ch.chrisport.continuousrewrite

import ch.chrisport.continuousrewrite.reporting.Discrepancy
import ch.chrisport.continuousrewrite.reporting.DiscrepancyReporter
import ch.chrisport.continuousrewrite.reporting.DiscrepancyType
import ch.chrisport.continuousrewrite.reporting.DiscrepancyType.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DiffReportingExtendedTest {

    @Test
    fun `given a complex obejct, when merge, then report discrepancies`() {
        val testReporter = TestDiscrepancyReporter()
        val migrationMerger = RewriteMerger(discrepancyReporter = testReporter)
        val original = mapOf(
            "ok_key" to "ok_value",
            "some_key" to "some_value",
            "some_object_key" to mapOf(
                "nested_key" to Pair("a_string", "not 42")
            ),
            "extras" to mapOf("extra_in_original_key" to 13)
        )
        val rewrite = mapOf(
            "ok_key" to "ok_value",
            "some_key" to "some_other_value",
            "some_object_key" to mapOf(
                "nested_key" to Pair("a_string", 42)
            ),
            "extras" to mapOf("extra_in_rewrite_key" to 13)
        )

        migrationMerger.merge(original, rewrite)

        println(testReporter.diffs)
        assertThat(testReporter.diffs).isEqualTo(
            listOf(
                Discrepancy(
                    original = "some_value",
                    rewrite = "some_other_value",
                    jsonPointer = "/some_key",
                    discrepancykind = VALUE_MISMATCH
                ),
                Discrepancy(
                    original = "not 42",
                    rewrite = 42,
                    jsonPointer = "/some_object_key/nested_key/second",
                    discrepancykind = VALUE_MISMATCH
                ),
                Discrepancy(
                    original = 13,
                    rewrite = null,
                    jsonPointer = "/extras/extra_in_original_key",
                    discrepancykind = MISSING_IN_REWRITE
                ),
                Discrepancy(
                    original = null,
                    rewrite = 13,
                    jsonPointer = "/extras/extra_in_rewrite_key",
                    discrepancykind = MISSING_IN_ORIGINAL
                )
            )
        )
    }
}
