package co.diwakar.trivia.database

import androidx.room.Database
import co.diwakar.trivia.dao.QuizDao
import co.diwakar.trivia.models.Quiz

@Database(entities = [Quiz::class], version = 1)
abstract class QuizDatabase {
    abstract fun quizDao(): QuizDao
}