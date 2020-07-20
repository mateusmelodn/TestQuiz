package com.mateusmelodn.android.testquiz.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mateusmelodn.android.testquiz.util.IntentKeys
import com.mateusmelodn.android.testquiz.activity.LanguageActivity
import com.mateusmelodn.android.testquiz.activity.MainActivity
import com.mateusmelodn.android.testquiz.R
import com.mateusmelodn.android.testquiz.db.entity.Language
import kotlinx.android.synthetic.main.language_card.view.*

class LanguageAdapter(private var languages: List<Language>, private val activity: MainActivity) :
    RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {
    // Provide a reference to the views for each data item
    class LanguageViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val mLanguageTitle: TextView = root.language_title_text_view
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.language_card, parent, false)
        return LanguageViewHolder(root)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        // Get current item
        val language = languages[position]

        // Update current cell
        holder.mLanguageTitle.text = language.title

        // Set onClick for current cell
        holder.itemView.setOnClickListener {
            onLanguageCardClick(language)
        }
    }

    // Pass data through intent
    private fun onLanguageCardClick(language: Language) {
        val intent = Intent(activity, LanguageActivity::class.java)
        intent.putExtra(IntentKeys.LANGUAGE_INTENT_KEY, language)
        activity.startActivity(intent)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = languages.size
}