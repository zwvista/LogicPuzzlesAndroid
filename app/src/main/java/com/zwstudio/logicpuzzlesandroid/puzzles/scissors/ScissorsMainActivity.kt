package com.zwstudio.logicpuzzlesandroid.puzzles.scissors

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class ScissorsMainActivity : GameMainActivity<ScissorsGame, ScissorsDocument, ScissorsGameMove, ScissorsGameState>() {
    private val document: ScissorsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, ScissorsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, ScissorsGameActivity::class.java))
    }
}

class ScissorsOptionsActivity : GameOptionsActivity<ScissorsGame, ScissorsDocument, ScissorsGameMove, ScissorsGameState>() {
    private val document: ScissorsDocument by inject()
    override val doc get() = document
}

class ScissorsHelpActivity : GameHelpActivity<ScissorsGame, ScissorsDocument, ScissorsGameMove, ScissorsGameState>() {
    private val document: ScissorsDocument by inject()
    override val doc get() = document
}
