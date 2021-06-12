package co.diwakar.trivia.quizsummary

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import co.diwakar.trivia.R
import co.diwakar.trivia.config.Extras
import co.diwakar.trivia.history.HistoryActivity
import co.diwakar.trivia.models.ActivityState
import co.diwakar.trivia.models.ErrorState
import co.diwakar.trivia.models.ProgressState
import co.diwakar.trivia.quiz.QuizActivity
import co.diwakar.trivia.utils.setupActionBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main._toolbar.*
import kotlinx.android.synthetic.main.activity_quiz_summary.*
import kotlinx.android.synthetic.main.activity_quiz_summary.progressOverlay
import java.io.IOException

@AndroidEntryPoint
class QuizSummaryActivity : AppCompatActivity() {

    private val summaryAdapter = QuizSummaryAdapter()
    private val summaViewModel: QuizSummaryViewModel by viewModels()

    /**
     * [setupExtras] to store the user name in userName from [summaViewModel]
     * then [setupViews] for setting up [summaryView] with [summaryAdapter]
     * setup [stateObserver]
     * after that fetch last attempted quiz.
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_summary)

        /**
         * setup custom action bar and title for the current screen
         * */
        setupActionBar(appToolbar)
        setTitle(R.string.last_quiz_summary)

        setupClicks()
        setupExtras()
        setupViews()
        summaViewModel.state.observe(this, stateObserver)
        summaViewModel.fetchLastSummary()
    }

    /**
     * set the user name in userName from [summaViewModel]
     * */
    private fun setupExtras() {
        intent.getStringExtra(Extras.USER_NAME)?.let { userName ->
            summaViewModel.userName = userName
            userMessageTxt.text = String.format("Hello! %s", userName)
        }
    }

    /**
     * set linear layout manager with [summaryView]
     * set [summaryAdapter] with [summaryView]
     * */
    private fun setupViews() {
        summaryView.layoutManager = LinearLayoutManager(this)
        summaryView.adapter = summaryAdapter
    }

    private fun setupClicks() {
        restartBtn.setOnClickListener {
            moveToQuizScreen()
        }

        historyBtn.setOnClickListener {
            moveToHistoryScreen()
        }
    }

    private val stateObserver: Observer<ActivityState> = Observer {
        when (it) {
            ProgressState -> {
                progressOverlay.visibility = View.VISIBLE
            }
            is QuizSummaryViewModel.FetchSummarySuccessState -> {
                progressOverlay.visibility = View.GONE
                summaryAdapter.addAll(it.summary)
                summaryAdapter.notifyDataSetChanged()
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
     * set user name as extras and start Quiz activity again
     * then finish this activity
     * */
    private fun moveToQuizScreen() {
        Intent(this@QuizSummaryActivity, QuizActivity::class.java).apply {
            putExtra(Extras.USER_NAME, summaViewModel.userName)
            startActivity(this)
        }
        finish()
    }

    /**
     * move to the [HistoryActivity] where all attempted quiz will be shown
     * */
    private fun moveToHistoryScreen() {
        Intent(this@QuizSummaryActivity, HistoryActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(progressOverlay, message, Snackbar.LENGTH_SHORT).show()
    }
}