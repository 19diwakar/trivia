package co.diwakar.trivia.quiz

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
class QuizViewModel @Inject constructor(private val quizRepository: QuizRepository) : ViewModel() {

    private val _state: MutableLiveData<ActivityState> = MutableLiveData()
    val state: LiveData<ActivityState> = _state

    private var currentPosition = 0
    private val questions = mutableListOf<Question>()

    private val _question: MutableLiveData<Question> = MutableLiveData()
    val question: LiveData<Question> = _question

    private val _questionStatus: MutableLiveData<Pair<Int, Int>> = MutableLiveData()
    val questionStatus: LiveData<Pair<Int, Int>> = _questionStatus

    var userName: String? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _state.value = ErrorState(throwable as Exception)
    }

    fun fetchQuestions() {
        _state.value = ProgressState
        if (questions.isEmpty()) {
            questions.addAll(quizRepository.fetchQuestions())
        }
        _questionStatus.value = Pair(currentPosition, questions.size)
        _question.value = questions[currentPosition]
        _state.value = InitialState
    }

    fun submitAnswer(answer: List<String>) {
        viewModelScope.launch(exceptionHandler) {
            _state.value = ProgressState
            questions[currentPosition].answer.addAll(answer)
            if (currentPosition == questions.size - 1) {
                submitQuiz()
            } else {
                currentPosition++
                _questionStatus.value = Pair(currentPosition, questions.size)
                _question.value = questions[currentPosition]
                _state.value = InitialState
            }
        }
    }

    private suspend fun submitQuiz() {
        val userName = this.userName
        _state.value = if (userName.isNullOrEmpty()) {
            InvalidUserState
        } else {
            quizRepository.addQuiz(userName, questions)
            QuizCompletedState
        }
    }

    object QuizCompletedState : ActivityState()
    object InvalidUserState : ActivityState()
}