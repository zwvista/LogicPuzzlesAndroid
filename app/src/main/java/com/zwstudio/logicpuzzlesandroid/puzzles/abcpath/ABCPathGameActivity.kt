package com.zwstudio.logicpuzzlesandroid.puzzles.abcpath

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class ABCPathGameActivity : GameGameActivity<ABCPathGame, ABCPathDocument, ABCPathGameMove, ABCPathGameState>() {
    private val document: ABCPathDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = ABCPathGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, ABCPathHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        ABCPathGame(level.layout, this, doc)
}
