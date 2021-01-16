package ch.chrisport.continuousrewrite

import ch.chrisport.continuousrewrite.reporting.Discrepancy
import ch.chrisport.continuousrewrite.reporting.DiscrepancyReporter
import ch.chrisport.continuousrewrite.reporting.DiscrepancyType
import ch.chrisport.continuousrewrite.reporting.DiscrepancyType.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DiffReportingTest {

    @Test
    fun `given a root field is different, when merge, then report difference in value`() {
        val testReporter = TestDiscrepancyReporter()
        val migrationMerger = RewriteMerger(discrepancyReporter = testReporter)
        val original = mapOf(
            "some_key" to "some_value"
        )
        val versionB = mapOf(
            "some_key" to "some_other_value"
        )

        migrationMerger.merge(original, versionB)

        assertThat(testReporter.diffs.size).isEqualTo(1)
        assertThat(testReporter.diffs[0]).isEqualTo(
            Discrepancy(
                original = "some_value",
                rewrite = "some_other_value",
                jsonPointer = "/some_key",
                discrepancykind = VALUE_MISMATCH
            )
        )
    }

    @Test
    fun `given a root field is missing in rewrite, when merge, then report MISSING_IN_REWRITE`() {
        val testReporter = TestDiscrepancyReporter()
        val migrationMerger = RewriteMerger(discrepancyReporter = testReporter)
        val original = mapOf(
            "some_key" to "some_value"
        )
        val rewrite = mapOf<String, String>()

        migrationMerger.merge(original, rewrite)

        assertThat(testReporter.diffs.size).isEqualTo(1)
        assertThat(testReporter.diffs[0]).isEqualTo(
            Discrepancy(
                original = "some_value",
                rewrite = null,
                jsonPointer = "/some_key",
                discrepancykind = MISSING_IN_REWRITE
            )
        )
    }

    @Test
    fun `given a root field is missing in original, when merge, then report MISSING_IN_ORIGINAL`() {
        val testReporter = TestDiscrepancyReporter()
        val migrationMerger = RewriteMerger(discrepancyReporter = testReporter)
        val original = mapOf<String, String>()
        val rewrite = mapOf(
            "some_key" to "some_value"
        )

        migrationMerger.merge(original, rewrite)

        assertThat(testReporter.diffs.size).isEqualTo(1)
        assertThat(testReporter.diffs[0]).isEqualTo(
            Discrepancy(
                original = null,
                rewrite = "some_value",
                jsonPointer = "/some_key",
                discrepancykind = MISSING_IN_ORIGINAL
            )
        )
    }

    @Test
    fun `given a nested field is missing in original, when merge, then report MISSING_IN_ORIGINAL`() {
        val testReporter = TestDiscrepancyReporter()
        val migrationMerger = RewriteMerger(discrepancyReporter = testReporter)
        val original = mapOf(
            "root_field" to mapOf<String, String>()

        )
        val rewrite = mapOf(
            "root_field" to mapOf(
                "nested_field" to "nested_value"
            )
        )

        migrationMerger.merge(original, rewrite)

        assertThat(testReporter.diffs.size).isEqualTo(1)
        assertThat(testReporter.diffs[0]).isEqualTo(
            Discrepancy(
                original = null,
                rewrite = "nested_value",
                jsonPointer = "/root_field/nested_field",
                discrepancykind = MISSING_IN_ORIGINAL
            )
        )
    }

    @Test
    fun `given a nested field is missing in rewrite, when merge, then report MISSING_IN_ORIGINAL`() {
        val testReporter = TestDiscrepancyReporter()
        val migrationMerger = RewriteMerger(discrepancyReporter = testReporter)
        val original = mapOf(
            "root_field" to mapOf(
                "nested_field" to "nested_value"
            )

        )
        val rewrite = mapOf(
            "root_field" to mapOf<String, String>()
        )

        migrationMerger.merge(original, rewrite)

        assertThat(testReporter.diffs.size).isEqualTo(1)
        assertThat(testReporter.diffs[0]).isEqualTo(
            Discrepancy(
                original = "nested_value",
                rewrite = null,
                jsonPointer = "/root_field/nested_field",
                discrepancykind = MISSING_IN_REWRITE
            )
        )
    }
}
