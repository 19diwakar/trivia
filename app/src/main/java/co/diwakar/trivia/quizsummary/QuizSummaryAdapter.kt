package co.diwakar.trivia.quizsummary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.diwakar.trivia.R
import co.diwakar.trivia.models.QuestionAnswer
import kotlinx.android.synthetic.main.activity_quiz.view.questionTxt
import kotlinx.android.synthetic.main.layout_quiz_summary_item.view.*

/**
 * In this we will show all attempted questions and answers
 * [summaryList] is used to store all attempted questions and answers
 * */
class QuizSummaryAdapter : RecyclerView.Adapter<SummaryViewHolder>() {

    private val summaryList = mutableListOf<QuestionAnswer>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_quiz_summary_item, parent, false)
        return SummaryViewHolder(view)
    }

    override fun getItemCount() = summaryList.size

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        holder.bind(summaryList[position])
    }

    fun addAll(toAdd: List<QuestionAnswer>) {
        summaryList.clear()
        summaryList.addAll(toAdd)
    }
}

class SummaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val questionTxt = itemView.questionTxt
    private val answerTxt = itemView.answerTxt

    fun bind(summary: QuestionAnswer) {
        questionTxt.text = String.format("Q. %s", summary.question)
        answerTxt.text = String.format("Ans. %s", summary.answer.joinToString(","))
    }
}