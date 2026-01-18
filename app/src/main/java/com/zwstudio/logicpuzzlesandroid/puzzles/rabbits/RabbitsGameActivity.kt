package com.zwstudio.logicpuzzlesandroid.puzzles.rabbits

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class RabbitsGameActivity : GameGameActivity<RabbitsGame, RabbitsDocument, RabbitsGameMove, RabbitsGameState>() {
    private val document: RabbitsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = RabbitsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, RabbitsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        RabbitsGame(level.layout, this, doc)
}