package co.diwakar.trivia.registration

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import co.diwakar.trivia.quiz.QuizActivity
import co.diwakar.trivia.R
import co.diwakar.trivia.config.Extras
import co.diwakar.trivia.utils.setupActionBar
import kotlinx.android.synthetic.main._toolbar.*
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        setupActionBar(appToolbar)
        setTitle(R.string.registration_title)

        startBtn.setOnClickListener {
            initRegistration()
        }
    }

    private fun initRegistration() {
        val userName = userNameInputLayout.editText?.text
        if (isValidUser(userName)) {
            moveToNextScreen(userName.toString())
        } else {
            userNameInputLayout.error = getString(R.string.please_enter_valid_user_name)
        }
    }

    private fun moveToNextScreen(userName: String) {
        Intent(this@RegistrationActivity, QuizActivity::class.java).apply {
            putExtra(Extras.USER_NAME, userName)
            startActivity(this)
        }
        finish()
    }

    private fun isValidUser(userName: Editable?): Boolean {
        return userName.isNullOrEmpty().not()
    }
}