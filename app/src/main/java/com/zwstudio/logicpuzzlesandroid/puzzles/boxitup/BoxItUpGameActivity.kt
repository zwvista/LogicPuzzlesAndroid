package com.zwstudio.logicpuzzlesandroid.puzzles.boxitup

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class BoxItUpGameActivity : GameGameActivity<BoxItUpGame, BoxItUpDocument, BoxItUpGameMove, BoxItUpGameState>() {
    private val document: BoxItUpDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = BoxItUpGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, BoxItUpHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        BoxItUpGame(level.layout, this, doc)
}