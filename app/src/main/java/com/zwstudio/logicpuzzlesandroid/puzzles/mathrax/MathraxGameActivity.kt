package com.zwstudio.logicpuzzlesandroid.puzzles.mathrax

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class MathraxGameActivity : GameGameActivity<MathraxGame, MathraxDocument, MathraxGameMove, MathraxGameState>() {
    private val document: MathraxDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = MathraxGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, MathraxHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        MathraxGame(level.layout, this, doc)
}