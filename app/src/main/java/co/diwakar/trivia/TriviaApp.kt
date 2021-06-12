package co.diwakar.trivia

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TriviaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        /*
        * Timber is using only for debug builds
        * */
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}