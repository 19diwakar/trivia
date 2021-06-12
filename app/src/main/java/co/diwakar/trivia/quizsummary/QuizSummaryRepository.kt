package co.diwakar.trivia.quizsummary

import co.diwakar.trivia.database.QuizDatabase
import co.diwakar.trivia.models.Question
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import javax.inject.Inject

class QuizSummaryRepository @Inject constructor(
    private val quizDatabase: QuizDatabase,
    private val gson: Gson
) {

    suspend fun getLastQuizSummary(): List<Question> {
        Timber.e("${quizDatabase.quizDao().getAll()}")
        val latestQuiz = quizDatabase.quizDao().getAll().first()
        return try {
            gson.fromJson(latestQuiz.submission, object : TypeToken<List<Question>>() {}.type)
        } catch (e: JsonSyntaxException) {
            emptyList()
        }
    }
}