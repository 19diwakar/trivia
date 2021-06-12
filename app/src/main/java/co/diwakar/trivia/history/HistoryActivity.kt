package co.diwakar.trivia.history

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import co.diwakar.trivia.R
import co.diwakar.trivia.models.ActivityState
import co.diwakar.trivia.models.ErrorState
import co.diwakar.trivia.models.ProgressState
import co.diwakar.trivia.utils.setupActionBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main._toolbar.*
import kotlinx.android.synthetic.main.activity_history.*
import java.io.IOException

@AndroidEntryPoint
class HistoryActivity : AppCompatActivity() {

    private val historyAdapter = HistoryAdapter()
    private val historyViewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        setupActionBar(appToolbar)
        setTitle(R.string.history)

        setupViews()
        historyViewModel.state.observe(this, stateObserver)
        historyViewModel.fetchHistory()
    }

    private fun setupViews() {
        historyView.layoutManager = LinearLayoutManager(this)
        historyView.adapter = historyAdapter
    }

    private val stateObserver: Observer<ActivityState> = Observer {
        when (it) {
            ProgressState -> {
                progressOverlay.visibility = View.VISIBLE
            }
            is HistoryViewModel.FetchHistorySuccessState -> {
                progressOverlay.visibility = View.GONE
                historyAdapter.addAll(it.history)
                historyAdapter.notifyDataSetChanged()
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