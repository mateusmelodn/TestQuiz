package com.mateusmelodn.android.testquiz.activity

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mateusmelodn.android.testquiz.R
import com.mateusmelodn.android.testquiz.db.entity.Language
import com.mateusmelodn.android.testquiz.db.entity.Quiz
import com.mateusmelodn.android.testquiz.db.entity.Word
import com.mateusmelodn.android.testquiz.util.IntentKeys.Companion.LANGUAGE_INTENT_KEY
import com.mateusmelodn.android.testquiz.util.IntentKeys.Companion.WORDS_INTENT_KEY
import kotlinx.android.synthetic.main.activity_quiz.*

class QuizActivity : AppCompatActivity() {
    // For animation use
    private lateinit var mFrontAnim: AnimatorSet
    private lateinit var mBackAnim: AnimatorSet
    private var mIsFrontCardShowing = true

    // Data will always come from LanguageActivity through intent
    // It'll be used to handle quiz logic
    private lateinit var mQuiz: Quiz

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Set up animation for flip card when user clicks buttons
        setUpAnimation()

        // Get data from LanguageActivity
        getIntentExtras()

        // Update UI
        updateCurrentPrimaryWordUI()
        updateCurrentTranslatedWordUI()
    }

    private fun setUpAnimation() {
        // Modify the camera scale
        val scale = applicationContext.resources.displayMetrics.density
        word_front_card_view.cameraDistance = 8000 * scale
        word_back_card_view.cameraDistance = 8000 * scale

        // Set animations
        mFrontAnim = AnimatorInflater.loadAnimator(
            applicationContext,
            R.animator.front_card_view
        ) as AnimatorSet
        mBackAnim = AnimatorInflater.loadAnimator(
            applicationContext,
            R.animator.back_card_view
        ) as AnimatorSet
    }

    private fun getIntentExtras() {
        // Get intent from LanguageActivity
        val currentLanguage = intent.getParcelableExtra<Language>(LANGUAGE_INTENT_KEY)!!
        val words = intent.getParcelableArrayListExtra<Word>(WORDS_INTENT_KEY)!!.toList()
        mQuiz = Quiz(
            currentLanguage,
            words
        )

        //Set title to action bar
        title = mQuiz.language.title
    }

    private fun updateCurrentPrimaryWordUI() {
        val currentWord = mQuiz.getCurrentWord()
        current_primary_word_text_view.text = currentWord.primaryWord
    }

    private fun updateCurrentTranslatedWordUI() {
        val currentWord = mQuiz.getCurrentWord()
        current_translated_word_text_view.text = currentWord.translatedWord
    }

    private fun flipCard(success: Boolean) {
        // Change views visibility
        show_answer_button.visibility = if (mIsFrontCardShowing) View.GONE else View.VISIBLE
        verify_answer_button.visibility = if (mIsFrontCardShowing) View.GONE else View.VISIBLE
        user_translation_input_layout.visibility =
            if (mIsFrontCardShowing) View.GONE else View.VISIBLE
        next_word_button.visibility = if (mIsFrontCardShowing) View.VISIBLE else View.GONE

        // Clear user input
        user_translation_edit_text.text = null
        user_translation_edit_text.error = null

        if (mIsFrontCardShowing) {
            // Set color according to result
            word_back_card_view.setBackgroundResource(if (success) R.drawable.word_card_success_background else R.drawable.word_card_failed_background)

            // Handle views visibility
            right_answer_image_view.visibility = if (success) View.VISIBLE else View.INVISIBLE
            wrong_answer_image_view.visibility = if (success) View.INVISIBLE else View.VISIBLE

            mFrontAnim.setTarget(word_front_card_view)
            mBackAnim.setTarget(word_back_card_view)

            mFrontAnim.start()
            mBackAnim.start()

            mIsFrontCardShowing = false
        } else {
            mFrontAnim.setTarget(word_back_card_view)
            mBackAnim.setTarget(word_front_card_view)

            mBackAnim.start()
            mFrontAnim.start()

            mIsFrontCardShowing = true
        }
    }

    // Linked to OnClick method in XML
    fun onShowAnswerButtonClick(view: View) {
        // Update UI after flipping card
        updateCurrentTranslatedWordUI()

        // Perform flip action
        flipCard(false)
    }

    // Linked to OnClick method in XML
    fun onNextWordButtonClick(view: View) {
        // Update current word
        mQuiz.updateWordIndex()

        // Update UI after flipping card
        updateCurrentPrimaryWordUI()

        // Perform flip action
        flipCard(false)
    }

    // Linked to OnClick method in XML
    fun onVerifyAnswerButtonClick(view: View) {
        // Update UI after flipping card
        updateCurrentTranslatedWordUI()

        // Get user translation
        val userTranslation = user_translation_edit_text.text.toString()

        if (!Word.isTranslatedWordValid(userTranslation)) {
            user_translation_edit_text.error = getString(R.string.invalid_translated_word)
            Toast.makeText(applicationContext, R.string.invalid_translated_word, Toast.LENGTH_LONG).show()
            return
        }

        // Verify whether user translation is right or wrong
        val success = mQuiz.verifyAnswer(userTranslation)

        // Flip card according to user translation
        flipCard(success)
    }
}