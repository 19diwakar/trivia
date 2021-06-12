package co.diwakar.trivia.quizsummary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.diwakar.trivia.models.ActivityState
import co.diwakar.trivia.models.ErrorState
import co.diwakar.trivia.models.ProgressState
import co.diwakar.trivia.models.QuestionAnswer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizSummaryViewModel @Inject constructor(private val quizSummaryRepository: QuizSummaryRepository) :
    ViewModel() {

    var userName: String? = null

    /**
     * [_state] updates all possible activity states according to that we update views
     * */
    private val _state: MutableLiveData<ActivityState> = MutableLiveData()
    val state: LiveData<ActivityState> = _state

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _state.value = ErrorState(throwable as Exception)
    }

    fun fetchLastSummary() {
        viewModelScope.launch(exceptionHandler) {
            _state.value = ProgressState
            val summary = quizSummaryRepository.getLastQuizSummary()
            _state.value = FetchSummarySuccessState(summary)
        }
    }

    data class FetchSummarySuccessState(val summary: List<QuestionAnswer>) : ActivityState()
}