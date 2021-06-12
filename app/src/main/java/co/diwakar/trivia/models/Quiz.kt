package co.diwakar.trivia.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "quiz_table")
data class Quiz(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val userName: String,
    val submission: String,
    val quizAttemptedAt: Long,
) {
    fun getQuestionsAnswers(): List<QuestionAnswer> {
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
        return try {
            gson.fromJson(submission, object : TypeToken<List<QuestionAnswer>>() {}.type)
        } catch (e: JsonSyntaxException) {
            emptyList()
        }
    }

    fun getQuizTime(): String {
        val dateFormat = SimpleDateFormat("dd MMM,yyyy hh:mm a", Locale.getDefault())
        return dateFormat.format(quizAttemptedAt)
    }
}