package com.zwstudio.logicpuzzlesandroid.puzzles.snail

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class SnailGameActivity : GameGameActivity<SnailGame, SnailDocument, SnailGameMove, SnailGameState>() {
    private val document: SnailDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = SnailGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, SnailHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        SnailGame(level.layout, this, doc)
}