package co.diwakar.trivia.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.diwakar.trivia.R
import co.diwakar.trivia.models.Quiz
import kotlinx.android.synthetic.main.layout_history_item.view.*
import kotlinx.android.synthetic.main.layout_question_answer_item.view.*

class HistoryAdapter : RecyclerView.Adapter<HistoryViewHolder>() {

    private val quizList = mutableListOf<Quiz>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount() = quizList.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val gameCount = quizList.size - position
        holder.bind(quizList[position], gameCount)
    }

    fun addAll(toAdd: List<Quiz>) {
        quizList.clear()
        quizList.addAll(toAdd)
    }
}

class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val gameStatusTxt = itemView.gameStatusTxt
    private val userNameTxt = itemView.userNameTxt
    private val questionAnswerContainer = itemView.questionAnswerContainer

    fun bind(quiz: Quiz, gameCount: Int) {
        gameStatusTxt.text = String.format("Game %d: %s", gameCount, quiz.getQuizTime())
        userNameTxt.text = String.format("Name: %s", quiz.userName)

        questionAnswerContainer.removeAllViews()
        quiz.getQuestionsAnswers().forEach {
            val questionAnswerView = LayoutInflater.from(itemView.context)
                .inflate(R.layout.layout_question_answer_item, null, false)

            questionAnswerView.questionTxt.text = String.format("Q. %s", it.value)
            questionAnswerView.answerTxt.text =
                String.format("Ans. %s", it.answer.joinToString(","))
            questionAnswerContainer.addView(questionAnswerView)
        }
    }
}