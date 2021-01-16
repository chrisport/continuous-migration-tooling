package ch.chrisport.continuousrewrite

import ch.chrisport.continuousrewrite.reporting.Discrepancy
import ch.chrisport.continuousrewrite.reporting.DiscrepancyReporter
import ch.chrisport.continuousrewrite.reporting.DiscrepancyType

class TestDiscrepancyReporter : DiscrepancyReporter {
    val diffs = mutableListOf<Discrepancy>()

    override fun report(
        original: Any?,
        rewrite: Any?,
        jsonPointer: String,
        discrepancykind: DiscrepancyType
    ) {
        diffs.add(Discrepancy(original, rewrite, jsonPointer, discrepancykind))
    }
}
