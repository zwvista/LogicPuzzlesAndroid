package com.zwstudio.logicpuzzlesandroid.common.android

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.domain.Game
import com.zwstudio.logicpuzzlesandroid.common.domain.GameState
import com.zwstudio.logicpuzzlesandroid.databinding.ActivityGameMainBinding

abstract class GameMainActivity<G : Game<G, GM, GS>, GD : GameDocument<GM>, GM, GS : GameState<GM>> : AppCompatActivity() {
    abstract val doc: GD
    var currentPage = 0
    var countPerPage = 12
    var numPages = 1

    protected lateinit var binding: ActivityGameMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val onClickListener = View.OnClickListener { v ->
            doc.selectedLevelID = (v as Button).text.toString()
            resumeGame()
        }
        // http://stackoverflow.com/questions/25905086/multiple-buttons-onclicklistener-android
        for (i in 0 until countPerPage) {
            val resID = resources.getIdentifier("btnLevel" + (i + 1), "id", "com.zwstudio.logicpuzzlesandroid")
            val button = findViewById<Button>(resID)
            button.setOnClickListener(onClickListener)
        }
        binding.btnPrevPage.setOnClickListener {
            currentPage = (currentPage - 1 + numPages) % numPages
            showCurrentPage()
        }
        binding.btnNextPage.setOnClickListener {
            currentPage = (currentPage + 1) % numPages
            showCurrentPage()
        }
        binding.btnResumeLevel.setOnClickListener {
            resumeGame()
        }
        binding.btnResetAllLevels.setOnClickListener {
            yesNoDialog("Do you really want to reset the options?") { doc.resetAllLevels() }
        }

        numPages = (doc.levels.size + countPerPage - 1) / countPerPage
        val index = doc.levels.indexOfFirst { it.id == doc.selectedLevelID }.coerceAtLeast(0)
        currentPage = index / countPerPage
        if (numPages == 1) {
            binding.btnPrevPage.visibility = View.INVISIBLE
            binding.btnNextPage.visibility = View.INVISIBLE
        }
        showCurrentPage()
        binding.tvGame.text = doc.gameTitle
        val toResume = intent.getBooleanExtra("toResume", false)
        if (toResume) resumeGame()
    }

    override fun onResume() {
        super.onResume()
        binding.btnResumeLevel.text = "Resume Level " + doc.selectedLevelID
    }

    private fun showCurrentPage() {
        for (i in 0 until countPerPage) {
            val resID = resources.getIdentifier("btnLevel" + (i + 1), "id", "com.zwstudio.logicpuzzlesandroid")
            val button = findViewById<Button>(resID)
            val index = currentPage * countPerPage + i
            val b = index < doc.levels.size
            button.visibility = if (b) View.VISIBLE else View.INVISIBLE
            if (b) button.text = doc.levels[index].id
        }
    }

    protected abstract fun resumeGame()
}