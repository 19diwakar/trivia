package co.diwakar.trivia.quiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import co.diwakar.trivia.R
import co.diwakar.trivia.config.Extras
import co.diwakar.trivia.models.*
import co.diwakar.trivia.quizsummary.QuizSummaryActivity
import co.diwakar.trivia.utils.setupActionBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main._toolbar.*
import kotlinx.android.synthetic.main.activity_quiz.*
import java.io.IOException

@AndroidEntryPoint
class QuizActivity : AppCompatActivity() {

    private val quizViewModel: QuizViewModel by viewModels()

    private val optionSelectionListener = object : OptionSelectionListener {
        override fun onOptionClicked(isAtLeastSingleSelected: Boolean) {
            /**
             * if at least one option is selected only then user can move to the next question
             * */
            nextBtn.isEnabled = isAtLeastSingleSelected
        }
    }
    private val quizOptionAdapter = QuizOptionsAdapter(optionSelectionListener)

    /**
     * [setupExtras] to store the user name in userName from [quizViewModel]
     * then [setupViews] for setting up [optionsView] with [quizOptionAdapter]
     * setup different observers ([stateObserver],[questionAnswerObserver], [questionStatusObserver])
     * after that fetch question list.
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        /**
         * setup custom action bar and title for the current screen
         * */
        setupActionBar(appToolbar)
        setTitle(R.string.triva_quiz)

        setupExtras()
        setupViews()
        setupClicks()

        quizViewModel.state.observe(this, stateObserver)
        quizViewModel.questionAnswer.observe(this, questionAnswerObserver)
        quizViewModel.questionStatus.observe(this, questionStatusObserver)
        quizViewModel.fetchQuestions()
    }

    /**
     * set the user name in userName from [quizViewModel]
     * */
    private fun setupExtras() {
        val userName = intent.getStringExtra(Extras.USER_NAME)
        quizViewModel.userName = userName
    }

    /**
     * set linear layout manager with [optionsView]
     * set [quizOptionAdapter] with [optionsView]
     * */
    private fun setupViews() {
        optionsView.layoutManager = LinearLayoutManager(this)
        optionsView.adapter = quizOptionAdapter
    }

    /**
     * on [nextBtn] click submit current question answer list of string.
     * */
    private fun setupClicks() {
        nextBtn.setOnClickListener {
            quizViewModel.submitAnswer(quizOptionAdapter.getSelectedOptions())
        }
    }

    /**
     * set question type message in [questionTypeTxt] whether it is single choice or multiple choice
     * setup data in [quizOptionAdapter] if it is not null
     * set [nextBtn] disable before loading next question
     * */
    private val questionAnswerObserver: Observer<QuestionAnswer> = Observer {
        if (it == null) {
            showMessage(getString(R.string.something_went_wrong))
        } else {
            questionTxt.text = String.format("Q. %s", it.question)
            val questionTypeMessage = if (it.isMultiSelect) {
                getString(R.string.multi_select_message)
            } else {
                getString(R.string.single_select_message)
            }
            questionTypeTxt.text = questionTypeMessage
            nextBtn.isEnabled = false
            quizOptionAdapter.isMultiSelected = it.isMultiSelect
            quizOptionAdapter.addAll(it.options, it.answer)
            quizOptionAdapter.notifyDataSetChanged()
        }
    }

    /**
     * set question number status in [questionStatusTxt]
     * */
    private val questionStatusObserver: Observer<Pair<Int, Int>> = Observer {
        it?.let { status ->
            /**
             * [status] is pair of int where first value is current question number and second value is total questions
             * */
            questionStatusTxt.text = String.format("Q%d of %d", status.first + 1, status.second)
        }
    }

    private val stateObserver: Observer<ActivityState> = Observer {
        when (it) {
            InitialState -> {
                progressOverlay.visibility = View.GONE
            }
            ProgressState -> {
                progressOverlay.visibility = View.VISIBLE
            }
            QuizViewModel.QuizCompletedState -> {
                progressOverlay.visibility = View.GONE
                moveToNextScreen()
            }
            QuizViewModel.InvalidUserState -> {
                progressOverlay.visibility = View.GONE
                showMessage(getString(R.string.invalid_user_name))
            }
            is ErrorState -> {
                progressOverlay.visibility = View.GONE
                val errorMessage = when (it.exception) {
                    is IOException -> {
                        it.exception.message
                    }
                    else -> {
                        null
                    }
                }

                showMessage(errorMessage ?: getString(R.string.something_went_wrong))
            }
        }
    }

    /**
     * set user name as extras and start quiz summary activity
     * then finish this activity
     * */
    private fun moveToNextScreen() {
        Intent(this@QuizActivity, QuizSummaryActivity::class.java).apply {
            putExtra(Extras.USER_NAME, quizViewModel.userName)
            startActivity(this)
        }
        finish()
    }

    private fun showMessage(message: String) {
        Snackbar.make(progressOverlay, message, Snackbar.LENGTH_SHORT).show()
    }
}