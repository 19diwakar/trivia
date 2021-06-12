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

    /**
     * [_state] updates all possible activity states according to that we update views
     * */
    private val _state: MutableLiveData<ActivityState> = MutableLiveData()
    val state: LiveData<ActivityState> = _state

    /**
     * [currentPosition] is the current question position
     * [questions] are the list of all questions
     * */
    private var currentPosition = 0
    private val questions = mutableListOf<QuestionAnswer>()

    /**
     * [_questionAnswer] use to observe current question
     * */
    private val _questionAnswer: MutableLiveData<QuestionAnswer> = MutableLiveData()
    val questionAnswer: LiveData<QuestionAnswer> = _questionAnswer

    /**
     * [_questionStatus] is pair of int where first value is current question number and second value is total questions
     * */
    private val _questionStatus: MutableLiveData<Pair<Int, Int>> = MutableLiveData()
    val questionStatus: LiveData<Pair<Int, Int>> = _questionStatus

    var userName: String? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _state.value = ErrorState(throwable as Exception)
    }

    /**
     * we fetch questions from [quizRepository] and add all to [questions] if [questions] are empty
     * update question status and set the current question
     * */
    fun fetchQuestions() {
        _state.value = ProgressState
        if (questions.isEmpty()) {
            questions.addAll(quizRepository.fetchQuestions())
        }
        _questionStatus.value = Pair(currentPosition, questions.size)
        _questionAnswer.value = questions[currentPosition]
        _state.value = InitialState
    }

    /**
     * set current question answer
     * if [currentPosition] is equal to the [questions] list size then submit quiz
     * otherwise load next question
     * */
    fun submitAnswer(answer: List<String>) {
        viewModelScope.launch(exceptionHandler) {
            _state.value = ProgressState
            questions[currentPosition].answer.addAll(answer)
            if (currentPosition == questions.size - 1) {
                submitQuiz()
            } else {
                currentPosition++
                _questionStatus.value = Pair(currentPosition, questions.size)
                _questionAnswer.value = questions[currentPosition]
                _state.value = InitialState
            }
        }
    }

    /**
     * if user name is empty then set [InvalidUserState]
     * otherwise add quiz to the local data base and set state to [QuizCompletedState]
     * */
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