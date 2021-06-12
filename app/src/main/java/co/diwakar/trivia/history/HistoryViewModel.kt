package co.diwakar.trivia.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.diwakar.trivia.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val historyRepository: HistoryRepository) :
    ViewModel() {

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