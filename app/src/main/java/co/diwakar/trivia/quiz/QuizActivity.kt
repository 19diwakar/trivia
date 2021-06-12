package co.diwakar.trivia.quiz

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import co.diwakar.trivia.R
import co.diwakar.trivia.config.Extras
import co.diwakar.trivia.models.*
import co.diwakar.trivia.utils.setupActionBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main._toolbar.*
import kotlinx.android.synthetic.main.activity_quiz.*
import java.io.IOException

@AndroidEntryPoint
class QuizActivity : AppCompatActivity() {

    private val quizViewModel: QuizViewModel by viewModels()
    private val quizOptionAdapter = QuizOptionsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        setupActionBar(appToolbar)
        setTitle(R.string.triva_quiz)

        setupExtras()
        setupViews()
        setupClicks()

        quizViewModel.state.observe(this, stateObserver)
        quizViewModel.question.observe(this, questionObserver)
        quizViewModel.questionStatus.observe(this, questionStatusObserver)
        quizViewModel.fetchQuestions()
    }


    private fun setupExtras() {
        val userName = intent.getStringExtra(Extras.USER_NAME)
        quizViewModel.userName = userName
    }

    private fun setupViews() {
        optionsView.layoutManager = LinearLayoutManager(this)
        optionsView.adapter = quizOptionAdapter
    }

    private fun setupClicks() {
        nextBtn.setOnClickListener {
            quizViewModel.submitAnswer(quizOptionAdapter.getSelectedOptions())
        }
    }

    private val questionObserver: Observer<Question> = Observer {
        if (it == null) {
            showMessage(getString(R.string.something_went_wrong))
        } else {
            questionTxt.text = String.format("Q. %s", it.value)
            val questionTypeMessage = if (it.isMultiSelect) {
                getString(R.string.multi_select_message)
            } else {
                getString(R.string.single_select_message)
            }
            questionTypeTxt.text = questionTypeMessage
            quizOptionAdapter.isMultiSelected = it.isMultiSelect
            quizOptionAdapter.addAll(it.options, it.answer ?: emptyList())
            quizOptionAdapter.notifyDataSetChanged()
        }
    }

    private val questionStatusObserver: Observer<Pair<Int, Int>> = Observer {
        it?.let { status ->
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
            }
            QuizViewModel.InvalidUserState -> {
                progressOverlay.visibility = View.GONE
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

    private fun showMessage(message: String) {
        Snackbar.make(progressOverlay, message, Snackbar.LENGTH_SHORT).show()
    }
}