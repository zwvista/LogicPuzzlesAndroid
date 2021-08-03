package com.zwstudio.logicpuzzlesandroid.puzzles.domino

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class DominoGameActivity : GameGameActivity<DominoGame, DominoDocument, DominoGameMove, DominoGameState>() {
    private val document: DominoDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = DominoGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, DominoHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        DominoGame(level.layout, this, doc)
}