package com.mateusmelodn.android.testquiz.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mateusmelodn.android.testquiz.R
import com.mateusmelodn.android.testquiz.db.AppDatabase
import com.mateusmelodn.android.testquiz.db.entity.Language
import com.mateusmelodn.android.testquiz.db.entity.Word
import com.mateusmelodn.android.testquiz.util.IntentKeys
import kotlinx.android.synthetic.main.activity_word_crud.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class WordCRUDActivity : AppCompatActivity() {
    // Object will always come when from LanguageActivity through intent
    private var mCurrentLanguage: Language? = null

    // Object which will be updated or deleted
    private var mCurrentWord: Word? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_crud)

        // Get data from LanguageActivity
        getLanguageIntentExtra()

        // Update UI
        updateWordUI()
    }

    private fun getLanguageIntentExtra() {
        // Get intent from LanguageActivity
        mCurrentLanguage = intent.getParcelableExtra(IntentKeys.LANGUAGE_INTENT_KEY)

        // Get intent from WordAdapter, after user clicks on a cell
        mCurrentWord = intent.getParcelableExtra(IntentKeys.WORD_INTENT_KEY)
    }

    private fun updateWordUI() {
        // mCurrentWord will have value only when user wants to update or delete a word
        mCurrentWord?.let {
            primary_word_edit_text.setText(it.primaryWord)
            translated_word_edit_text.setText(it.translatedWord)

            delete_word_button.visibility = View.VISIBLE
            update_word_fab.visibility = View.VISIBLE

            save_word_button.visibility = View.GONE
        }
    }

    // Linked to OnClick method in XML
    fun onSaveWordButtonClick(view: View) {
        GlobalScope.async {
            // Get data from UI
            val word = Word(
                0,
                mCurrentLanguage!!.title,
                primary_word_edit_text.text.toString(),
                translated_word_edit_text.text.toString()
            )

            if (!word.isPrimaryWordValid()) {
                withContext(Dispatchers.Main) {
                    primary_word_edit_text.error = getString(R.string.invalid_primary_word)
                    Toast.makeText(applicationContext, R.string.invalid_primary_word, Toast.LENGTH_LONG).show()
                }
                return@async
            }

            if (!word.isTranslatedWordValid()) {
                withContext(Dispatchers.Main) {
                    translated_word_edit_text.error = getString(R.string.invalid_translated_word)
                    Toast.makeText(applicationContext, R.string.invalid_translated_word, Toast.LENGTH_LONG).show()
                }
                return@async
            }

            // Save data in DB
            AppDatabase.saveWord(word, applicationContext)

            // Finish current activity
            finish()
        }
    }

    // Linked to OnClick method in XML
    fun onDeleteWordButtonClick(view: View) {
        GlobalScope.async {
            // Save data in DB
            AppDatabase.deleteWord(mCurrentWord!!, applicationContext)

            // Finish current activity
            finish()
        }
    }

    // Linked to OnClick method in XML
    fun onUpdateWordFABClick(view: View) {
        GlobalScope.async {
            // Update data
            mCurrentWord!!.primaryWord = primary_word_edit_text.text.toString()
            mCurrentWord!!.translatedWord = translated_word_edit_text.text.toString()

            if (!mCurrentWord!!.isPrimaryWordValid()) {
                withContext(Dispatchers.Main) {
                    primary_word_edit_text.error = getString(R.string.invalid_primary_word)
                    Toast.makeText(applicationContext, R.string.invalid_primary_word, Toast.LENGTH_LONG).show()
                }
                return@async
            }

            if (!mCurrentWord!!.isTranslatedWordValid()) {
                withContext(Dispatchers.Main) {
                    translated_word_edit_text.error = getString(R.string.invalid_translated_word)
                    Toast.makeText(applicationContext, R.string.invalid_translated_word, Toast.LENGTH_LONG).show()
                }
                return@async
            }

            // Save data in DB
            AppDatabase.updateWord(mCurrentWord!!, applicationContext)

            // Finish current activity
            finish()
        }
    }
}