package com.mateusmelodn.android.testquiz.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mateusmelodn.android.testquiz.R
import com.mateusmelodn.android.testquiz.adapter.WordAdapter
import com.mateusmelodn.android.testquiz.db.AppDatabase
import com.mateusmelodn.android.testquiz.db.entity.Language
import com.mateusmelodn.android.testquiz.db.entity.Word
import com.mateusmelodn.android.testquiz.util.IntentKeys.Companion.LANGUAGE_INTENT_KEY
import com.mateusmelodn.android.testquiz.util.IntentKeys.Companion.WORDS_INTENT_KEY
import kotlinx.android.synthetic.main.activity_language.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class LanguageActivity : AppCompatActivity() {
    // List of words which will be passed to QuizActivity if user click init quiz
    private lateinit var mWords: List<Word>

    // Object will always come from MainActivity or LanguageRegisterActivity through intent
    // When it comes from MainActivity, it will always have a valid id
    // When it comes from LanguageRegisterActivity, it will always have id = 0
    // A valid id means that DB can contain or not word for that language
    // An invalid id means that DB won't contain word for that language
    companion object {
        private lateinit var currentLanguage: Language
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)
        getLanguageIntentExtra()
    }

    override fun onStart() {
        super.onStart()
        //Fetch words from current language and update UI
        initRecyclerView(currentLanguage)
    }

    private fun getLanguageIntentExtra() {
        // Get intent from MainActivity or LanguageRegisterActivity
        // If no extra data is provided, it means user got back from WordRegisterActivity or QuizActivity
        currentLanguage = intent.getParcelableExtra(LANGUAGE_INTENT_KEY) ?: currentLanguage

        //Set title to action bar
        title = currentLanguage.title
    }

    private fun setWordsToRecyclerView(words: List<Word>) {
        val viewManager = LinearLayoutManager(this)
        val viewAdapter = WordAdapter(words, this)

        words_recycler_view.apply {
            // Use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // Use a linear layout manager
            layoutManager = viewManager

            // Specify an viewAdapter
            adapter = viewAdapter
        }
    }

    // Fetch data and update UI
    private fun initRecyclerView(language: Language) {
        GlobalScope.async {
            mWords = withContext(Dispatchers.IO) {
                // Fetch data in background thread
                AppDatabase.getAllWordsFromLanguage(language.title, applicationContext)
            }
            withContext(Dispatchers.Main) {
                // Handle views visibility
                word_info_text_view.visibility =
                    if (mWords.isNotEmpty()) View.GONE else View.VISIBLE
                words_recycler_view.visibility =
                    if (mWords.isNotEmpty()) View.VISIBLE else View.GONE
                init_quiz_button.visibility = if (mWords.isNotEmpty()) View.VISIBLE else View.GONE

                // Show data in UI
                if (mWords.isNotEmpty()) {
                    setWordsToRecyclerView(mWords)
                }
            }
        }
    }

    // Linked to OnClick method in XML
    fun onAddWordFABClick(view: View) {
        // Pass created word to WordRegisterActivity
        val intent = Intent(this, WordCRUDActivity::class.java)
        intent.putExtra(LANGUAGE_INTENT_KEY, currentLanguage)
        startActivity(intent)
    }

    // Linked to OnClick method in XML
    fun onStartQuizButtonClick(view: View) {
        // Pass current language its words for QuizActivity
        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra(LANGUAGE_INTENT_KEY, currentLanguage)
        val wordsArrayList = mWords.toTypedArray().toCollection(ArrayList())
        intent.putParcelableArrayListExtra(WORDS_INTENT_KEY, wordsArrayList)
        startActivity(intent)
    }
}