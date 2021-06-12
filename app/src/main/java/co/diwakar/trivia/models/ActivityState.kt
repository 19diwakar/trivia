package co.diwakar.trivia.models

import java.lang.Exception

open class ActivityState

object InitialState: ActivityState()
object ProgressState: ActivityState()
data class ErrorState(val exception: Exception): ActivityState()