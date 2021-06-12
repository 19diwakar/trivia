package co.diwakar.trivia.models

data class Question(
    val value: String,
    val options: List<String>,
    val isMultiSelect: Boolean,
    var answer: MutableList<String> = mutableListOf()
)