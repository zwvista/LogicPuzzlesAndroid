package com.zwstudio.logicpuzzlesandroid.puzzles.loopy

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class LoopyGameActivity : GameGameActivity<LoopyGame, LoopyDocument, LoopyGameMove, LoopyGameState>() {
    private val document: LoopyDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = LoopyGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, LoopyHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        LoopyGame(level.layout, this, doc)
}