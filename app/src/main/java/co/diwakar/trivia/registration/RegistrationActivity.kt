package co.diwakar.trivia.registration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.diwakar.trivia.R
import co.diwakar.trivia.utils.setupActionBar
import kotlinx.android.synthetic.main._toolbar.*

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        setupActionBar(appToolbar)
        setTitle(R.string.registration_title)
    }
}