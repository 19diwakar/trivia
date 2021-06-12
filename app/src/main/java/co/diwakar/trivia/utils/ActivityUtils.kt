package co.diwakar.trivia.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

fun AppCompatActivity.setupActionBar(toolbar: Toolbar, homeAsUpEnabled: Boolean = true) {
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUpEnabled)
    toolbar.setNavigationOnClickListener { finish() }
}