package com.mateusmelodn.android.testquiz.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.mateusmelodn.android.testquiz.R
import com.mateusmelodn.android.testquiz.adapter.LanguageAdapter
import com.mateusmelodn.android.testquiz.db.AppDatabase
import com.mateusmelodn.android.testquiz.db.entity.Language
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        initRecyclerView()
    }

    private fun setLanguagesToRecyclerView(languages: List<Language>) {
        val viewManager = GridLayoutManager(this, 2)
        val viewAdapter = LanguageAdapter(
            languages,
            this
        )

        // languages_recycler_view can be accessed without using findViewById method, but it's the same.
        languages_recycler_view.apply {
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
    private fun initRecyclerView() {
        GlobalScope.async {
            val languages = withContext(Dispatchers.IO) {
                // Fetch data in background thread
                AppDatabase.getAllLanguages(applicationContext)
            }
            withContext(Dispatchers.Main) {
                // Handle views visibility
                language_info_text_view.visibility =
                    if (languages.isNotEmpty()) View.GONE else View.VISIBLE
                languages_recycler_view.visibility =
                    if (languages.isNotEmpty()) View.VISIBLE else View.GONE

                // Show data in UI
                if (languages.isNotEmpty()) {
                    setLanguagesToRecyclerView(languages)
                }
            }
        }
    }

    // Linked to OnClick method in XML
    fun onAddLanguageFABClick(view: View) {
        startActivity(Intent(this, LanguageRegisterActivity::class.java))
    }
}