package co.diwakar.trivia.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.diwakar.trivia.models.ActivityState
import co.diwakar.trivia.models.ErrorState
import co.diwakar.trivia.models.ProgressState
import co.diwakar.trivia.models.Quiz
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val historyRepository: HistoryRepository) :
    ViewModel() {

    /**
     * [_state] updates all possible activity states according to that we update views
     * */
    private val _state: MutableLiveData<ActivityState> = MutableLiveData()
    val state: LiveData<ActivityState> = _state

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _state.value = ErrorState(throwable as Exception)
    }

    fun fetchHistory() {
        viewModelScope.launch(exceptionHandler) {
            _state.value = ProgressState
            val summary = historyRepository.getAllQuiz()
            _state.value = FetchHistorySuccessState(summary)
        }
    }

    data class FetchHistorySuccessState(val history: List<Quiz>) : ActivityState()
}