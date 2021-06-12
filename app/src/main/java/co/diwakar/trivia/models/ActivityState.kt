package co.diwakar.trivia.models

import java.lang.Exception

open class ActivityState

object InitialState: ActivityState()
object ProgressState: ActivityState()
object SuccessState: ActivityState()
data class ErrorState(val exception: Exception): ActivityState()