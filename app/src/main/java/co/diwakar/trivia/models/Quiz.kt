package co.diwakar.trivia.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_table")
data class Quiz(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val userName: String,
    val questionOneAnswer: String,
    val questionTwoAnswer: String,
    val quizAttemptedAt: Long,
)