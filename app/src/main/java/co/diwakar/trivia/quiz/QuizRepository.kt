package co.diwakar.trivia.quiz

import co.diwakar.trivia.database.QuizDatabase
import co.diwakar.trivia.models.Question
import co.diwakar.trivia.models.Quiz
import com.google.gson.Gson
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val quizDatabase: QuizDatabase,
    private val gson: Gson
) {

    suspend fun addQuiz(userName: String, submissions: List<Question>) {
        val quiz = Quiz(
            id = 0,
            userName = userName,
            submission = gson.toJson(submissions),
            quizAttemptedAt = System.currentTimeMillis()
        )
        quizDatabase.quizDao().addQuiz(quiz)
    }

    fun fetchQuestions(): List<Question> {
        return listOf(questionOne, questionTwo)
    }

    private val questionOne = Question(
        value = "Who is the best cricketer in the world?",
        options = listOf("Sachin Tendulkar", "Virat Kolli", "Adam Gilchirst", "Jacques Kallis"),
        isMultiSelect = false
    )

    private val questionTwo = Question(
        value = "What are the colors in the Indian national flag?",
        options = listOf("White", "Yellow", "Orange", "Green"),
        isMultiSelect = true
    )
}