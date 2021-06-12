package co.diwakar.trivia.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.diwakar.trivia.R
import kotlinx.android.synthetic.main.layout_option_item.view.*

class QuizOptionsAdapter : RecyclerView.Adapter<OptionViewHolder>() {

    private val options = mutableListOf<String>()
    private val selectedOptions: MutableList<String> = mutableListOf()
    var isMultiSelected: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_option_item, parent, false)
        return OptionViewHolder(view, clickListener)
    }

    override fun getItemCount(): Int {
        return options.size
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        val isSelected = selectedOptions.contains(options[position])
        holder.onBind(options[position], position, isSelected)
    }

    fun addAll(options: List<String>, selectedOptions: List<String>) {
        this.options.clear()
        this.options.addAll(options)
        this.selectedOptions.clear()
        this.selectedOptions.addAll(selectedOptions)
    }

    private val clickListener = View.OnClickListener {
        val position = it.getTag(R.id.position) as Int
        val selectedOption = options[position]

        if (selectedOptions.contains(selectedOption).not()) {
            if (isMultiSelected.not()) {
                selectedOptions.clear()
            }
            selectedOptions.add(selectedOption)
        } else {
            selectedOptions.remove(selectedOption)
        }
        notifyDataSetChanged()
    }

    fun getSelectedOptions(): List<String> {
        return selectedOptions
    }
}

class OptionViewHolder(itemView: View, private val onClickListener: View.OnClickListener) :
    RecyclerView.ViewHolder(itemView) {

    private val optionValueTxt: TextView = itemView.optionValueTxt
    private val selectionStateIv = itemView.selectionStateIv

    fun onBind(option: String, position: Int, isSelected: Boolean) {
        optionValueTxt.text = option
        selectionStateIv.isSelected = isSelected
        itemView.setOnClickListener(onClickListener)
        itemView.setTag(R.id.position, position)
    }
}