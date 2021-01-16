package ch.chrisport.continuousrewrite.reporting

data class Discrepancy(
    val original: Any?,
    val rewrite: Any?,
    val jsonPointer: String,
    val discrepancykind: DiscrepancyType
)


// how about this, or is this overkill?
interface FieldDiscrepancy {
    val jsonPointer: String
}

sealed class DiscrepancyVariant() : FieldDiscrepancy
data class ValueMismatch(val original: Any?, val rewrite: Any?, override val jsonPointer: String) : DiscrepancyVariant()
data class MissingInOriginal(val rewrite: Any?, override val jsonPointer: String) : DiscrepancyVariant()
data class MissingInRewrite(val original: Any?, override val jsonPointer: String) : DiscrepancyVariant()

fun main() {
    // example
    val discrepancy: FieldDiscrepancy = ValueMismatch("some", "other", "pointer")

    when (discrepancy){
        is MissingInOriginal -> discrepancy.rewrite
    }
}
