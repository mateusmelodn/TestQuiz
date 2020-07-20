package com.mateusmelodn.android.testquiz.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mateusmelodn.android.testquiz.activity.LanguageActivity
import com.mateusmelodn.android.testquiz.R
import com.mateusmelodn.android.testquiz.activity.WordCRUDActivity
import com.mateusmelodn.android.testquiz.db.entity.Word
import com.mateusmelodn.android.testquiz.util.IntentKeys.Companion.WORD_INTENT_KEY
import kotlinx.android.synthetic.main.word_card.view.*

class WordAdapter(
    private var words: List<Word>, private val activity: LanguageActivity
) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {
    // Provide a reference to the views for each data item
    class WordViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val mPrimaryWord: TextView = root.primary_word_text_view
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.word_card, parent, false)
        return WordViewHolder(root)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        // Get current item
        val word = words[position]

        // Update current cell
        holder.mPrimaryWord.text = word.primaryWord

        // Set onClick for current cell
        holder.itemView.setOnClickListener {
            onWordCardClick(word)
        }
    }

    // Pass data through intent
    private fun onWordCardClick(word: Word) {
        val intent = Intent(activity, WordCRUDActivity::class.java)
        intent.putExtra(WORD_INTENT_KEY, word)
        activity.startActivity(intent)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = words.size
}