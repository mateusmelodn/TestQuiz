package com.mateusmelodn.android.testquiz.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mateusmelodn.android.testquiz.R
import com.mateusmelodn.android.testquiz.db.AppDatabase
import com.mateusmelodn.android.testquiz.db.entity.Language
import com.mateusmelodn.android.testquiz.util.IntentKeys
import kotlinx.android.synthetic.main.activity_language_register.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class LanguageRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_register)
    }

    private fun startLanguageActivityAndFinishCurrent(language: Language) {
        // Pass created language to LanguageActivity
        val intent = Intent(this, LanguageActivity::class.java)
        intent.putExtra(IntentKeys.LANGUAGE_INTENT_KEY, language)
        startActivity(intent)

        // Finish current activity in order to avoid getting back to LanguageRegisterActivity from LanguageActivity
        finish()
    }

    // Linked to OnClick method in XML
    fun onDiscardLanguageFABClick(view: View) {
        finish()
    }

    // Linked to OnClick method in XML
    fun onSaveLanguageFABClick(view: View) {
        GlobalScope.async {
            // Get data from UI
            val language = Language(language_value_edit_text.text.toString())

            if (language.isTitleValid()) {
                // Save data in DB
                AppDatabase.saveLanguage(language, applicationContext)

                // Start LanguageActivity and finish current
                startLanguageActivityAndFinishCurrent(language)
            } else {
                withContext(Dispatchers.Main) {
                    language_value_edit_text.error = getString(R.string.invalid_language)
                    Toast.makeText(applicationContext, R.string.invalid_language, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}