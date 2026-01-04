package com.zwstudio.logicpuzzlesandroid.puzzles.chocolate

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class ChocolateGameActivity : GameGameActivity<ChocolateGame, ChocolateDocument, ChocolateGameMove, ChocolateGameState>() {
    private val document: ChocolateDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = ChocolateGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, ChocolateHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        ChocolateGame(level.layout, this, doc)
}