package co.diwakar.trivia.models

data class QuestionAnswer(
    val value: String,
    val options: List<String>,
    val isMultiSelect: Boolean,
    var answer: MutableList<String> = mutableListOf()
)