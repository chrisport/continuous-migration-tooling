package ch.chrisport.continuousrewrite

// similar to JsonPointer
typealias FieldPointer = String
fun fieldPointer(fieldName: String, parent: String = ""): FieldPointer = "$parent/$fieldName"
