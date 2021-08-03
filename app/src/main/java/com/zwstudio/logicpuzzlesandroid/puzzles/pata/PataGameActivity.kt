package com.zwstudio.logicpuzzlesandroid.puzzles.pata

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class PataGameActivity : GameGameActivity<PataGame, PataDocument, PataGameMove, PataGameState>() {
    private val document: PataDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = PataGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, PataHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        PataGame(level.layout, this, doc)
}