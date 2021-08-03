package com.zwstudio.logicpuzzlesandroid.puzzles.square100

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class Square100GameActivity : GameGameActivity<Square100Game, Square100Document, Square100GameMove, Square100GameState>() {
    private val document: Square100Document by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = Square100GameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, Square100HelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        Square100Game(level.layout, this, doc)
}