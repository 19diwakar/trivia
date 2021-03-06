package co.diwakar.trivia.history

import co.diwakar.trivia.database.QuizDatabase
import co.diwakar.trivia.models.Quiz
import javax.inject.Inject

class HistoryRepository @Inject constructor(private val quizDatabase: QuizDatabase) {

    /**
     * will return all attempted quiz stored in Quiz database
     * */
    suspend fun getAllQuiz(): List<Quiz> {
        return quizDatabase.quizDao().getAll()
    }
}