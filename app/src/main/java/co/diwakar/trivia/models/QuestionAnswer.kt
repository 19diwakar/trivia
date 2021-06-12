package co.diwakar.trivia.models

/**
 * [options] are all possible options for that [question]
 * if [isMultiSelect] is true then user can select more then one option other only one option
 * can be selected
 * */
data class QuestionAnswer(
    val question: String,
    val options: List<String>,
    val isMultiSelect: Boolean,
    var answer: MutableList<String> = mutableListOf()
)