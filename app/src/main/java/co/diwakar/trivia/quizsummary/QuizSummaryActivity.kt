package co.diwakar.trivia.quizsummary

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import co.diwakar.trivia.R
import co.diwakar.trivia.config.Extras
import co.diwakar.trivia.models.ActivityState
import co.diwakar.trivia.models.ErrorState
import co.diwakar.trivia.models.ProgressState
import co.diwakar.trivia.utils.setupActionBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main._toolbar.*
import kotlinx.android.synthetic.main.activity_quiz_summary.*
import java.io.IOException

@AndroidEntryPoint
class QuizSummaryActivity : AppCompatActivity() {

    private val summaryAdapter = QuizSummaryAdapter()
    private val summaViewModel: QuizSummaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_summary)

        setupActionBar(appToolbar)
        setTitle(R.string.last_quiz_summary)

        setupExtras()
        setupViews()
        summaViewModel.state.observe(this, stateObserver)
        summaViewModel.fetchLastSummary()
    }

    private fun setupExtras() {
        intent.getStringExtra(Extras.USER_NAME)?.let { userName ->
            userMessageTxt.text = String.format("Hello! %s", userName)
        }
    }

    private fun setupViews() {
        summaryView.layoutManager = LinearLayoutManager(this)
        summaryView.adapter = summaryAdapter
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

    private fun showMessage(message: String) {
        Snackbar.make(progressOverlay, message, Snackbar.LENGTH_SHORT).show()
    }
}