package com.zwstudio.logicpuzzlesandroid.puzzles.wildlifepark

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class WildlifeParkGameActivity : GameGameActivity<WildlifeParkGame, WildlifeParkDocument, WildlifeParkGameMove, WildlifeParkGameState>() {
    private val document: WildlifeParkDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = WildlifeParkGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, WildlifeParkHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        WildlifeParkGame(level.layout, this, doc)
}