package com.zwstudio.logicpuzzlesandroid.puzzles.landscapes

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class LandscapesMainActivity : GameMainActivity<LandscapesGame, LandscapesDocument, LandscapesGameMove, LandscapesGameState>() {
    private val document: LandscapesDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, LandscapesOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, LandscapesGameActivity::class.java))
    }
}

class LandscapesOptionsActivity : GameOptionsActivity<LandscapesGame, LandscapesDocument, LandscapesGameMove, LandscapesGameState>() {
    private val document: LandscapesDocument by inject()
    override val doc get() = document
}

class LandscapesHelpActivity : GameHelpActivity<LandscapesGame, LandscapesDocument, LandscapesGameMove, LandscapesGameState>() {
    private val document: LandscapesDocument by inject()
    override val doc get() = document
}