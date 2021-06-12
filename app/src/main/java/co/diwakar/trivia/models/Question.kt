package co.diwakar.trivia.models

data class Question(
    val value: String,
    val options: List<String>,
    val isMultiSelect: Boolean,
    var isAttempted: Boolean? = false,
    var answer: List<String>? = null
)