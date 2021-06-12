package co.diwakar.trivia.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.diwakar.trivia.R
import co.diwakar.trivia.models.Quiz
import kotlinx.android.synthetic.main.layout_history_item.view.*
import kotlinx.android.synthetic.main.layout_question_answer_item.view.*

/**
 * In this we will show all attempted quiz
 * [history] is used to store all attempted quiz
 * */
class HistoryAdapter : RecyclerView.Adapter<HistoryViewHolder>() {

    private val history = mutableListOf<Quiz>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount() = history.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        /**
         * gameCount is the attempted quiz number
         * */
        val gameCount = history.size - position
        holder.bind(history[position], gameCount)
    }

    fun addAll(toAdd: List<Quiz>) {
        history.clear()
        history.addAll(toAdd)
    }
}

class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val gameStatusTxt = itemView.gameStatusTxt
    private val userNameTxt = itemView.userNameTxt
    private val questionAnswerContainer = itemView.questionAnswerContainer

    fun bind(quiz: Quiz, gameCount: Int) {
        gameStatusTxt.text = String.format("Game %d: %s", gameCount, quiz.getQuizTime())
        userNameTxt.text = String.format("Name: %s", quiz.userName)

        /**
         * remove all views from [questionAnswerContainer]
         * then add all attempted question and answer views with it
         * [questionAnswerContainer] is a linear layout with vertical orientation
         * */
        questionAnswerContainer.removeAllViews()
        quiz.getQuestionsAnswers().forEach {
            val questionAnswerView = LayoutInflater.from(itemView.context)
                .inflate(R.layout.layout_question_answer_item, null, false)

            questionAnswerView.questionTxt.text = String.format("Q. %s", it.question)
            questionAnswerView.answerTxt.text =
                String.format("Ans. %s", it.answer.joinToString(","))
            questionAnswerContainer.addView(questionAnswerView)
        }
    }
}