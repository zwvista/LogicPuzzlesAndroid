package com.zwstudio.logicpuzzlesandroid.puzzles.landscaper

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class LandscaperMainActivity : GameMainActivity<LandscaperGame, LandscaperDocument, LandscaperGameMove, LandscaperGameState>() {
    private val document: LandscaperDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, LandscaperOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, LandscaperGameActivity::class.java))
    }
}

class LandscaperOptionsActivity : GameOptionsActivity<LandscaperGame, LandscaperDocument, LandscaperGameMove, LandscaperGameState>() {
    private val document: LandscaperDocument by inject()
    override val doc get() = document
}

class LandscaperHelpActivity : GameHelpActivity<LandscaperGame, LandscaperDocument, LandscaperGameMove, LandscaperGameState>() {
    private val document: LandscaperDocument by inject()
    override val doc get() = document
}