package co.diwakar.trivia.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.diwakar.trivia.R
import kotlinx.android.synthetic.main.layout_option_item.view.*

/**
 * In this we will show all possible options related to question and will handle option selection process
 * [options] are all possible options
 * [selectedOptions] are selected options by user
 * [isMultiSelected] will be true if multi selection question otherwise it will be false
 * */
class QuizOptionsAdapter(private val optionSelectionListener: OptionSelectionListener) :
    RecyclerView.Adapter<OptionViewHolder>() {

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
        /**
         * isSelected will be true if [selectedOptions] contains it otherwise false
         * */
        val isSelected = selectedOptions.contains(options[position])
        holder.onBind(options[position], position, isSelected)
    }

    /**
     * clear and add all possible options into [options] and all selected options into [selectedOptions]
     * */
    fun addAll(options: List<String>, selectedOptions: List<String>) {
        this.options.clear()
        this.options.addAll(options)
        this.selectedOptions.clear()
        this.selectedOptions.addAll(selectedOptions)
    }

    /**
     * handle view holder item click listener
     * */
    private val clickListener = View.OnClickListener {
        val position = it.getTag(R.id.postion) as Int
        val selectedOption = options[position]

        /**
         * if [selectedOptions] does not contain current selected option then we add selected option
         * otherwise if already selected then remove that option from [selectedOptions]
         * */
        if (selectedOptions.contains(selectedOption).not()) {
            /**
             * if [isMultiSelected] is false then clear [selectedOptions] first
             * */
            if (isMultiSelected.not()) {
                selectedOptions.clear()
            }
            selectedOptions.add(selectedOption)
        } else {
            selectedOptions.remove(selectedOption)
        }
        /**
         * if [selectedOptions] are not empty then set isAtLeastSingleSelected true for
         * [optionSelectionListener] otherwise set it to false
         * */
        optionSelectionListener.onOptionClicked(selectedOption.isNotEmpty())
        notifyDataSetChanged()
    }

    /**
     * will return all selected options
     * */
    fun getSelectedOptions(): List<String> {
        return selectedOptions
    }
}

/**
 * In this class we add option data with views
 * */
class OptionViewHolder(itemView: View, private val onClickListener: View.OnClickListener) :
    RecyclerView.ViewHolder(itemView) {

    private val optionValueTxt: TextView = itemView.optionValueTxt
    private val selectionStateIv = itemView.selectionStateIv

    fun onBind(option: String, position: Int, isSelected: Boolean) {
        optionValueTxt.text = option
        selectionStateIv.isSelected = isSelected
        itemView.setOnClickListener(onClickListener)
        itemView.setTag(R.id.postion, position)
    }
}

interface OptionSelectionListener {
    fun onOptionClicked(isAtLeastSingleSelected: Boolean)
}