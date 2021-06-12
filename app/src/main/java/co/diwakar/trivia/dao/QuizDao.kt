package co.diwakar.trivia.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.diwakar.trivia.models.Quiz

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addQuiz(toAdd: Quiz)

    @Query("select * from quiz_table ORDER BY quizAttemptedAt DESC")
    suspend fun getAll(): PagingSource<Int, Quiz>
}