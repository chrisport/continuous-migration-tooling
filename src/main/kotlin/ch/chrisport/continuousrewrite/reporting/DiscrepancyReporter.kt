package ch.chrisport.continuousrewrite.reporting

interface DiscrepancyReporter {
    fun report(original: Any?, rewrite: Any?, jsonPointer: String, discrepancykind: DiscrepancyType)
}
