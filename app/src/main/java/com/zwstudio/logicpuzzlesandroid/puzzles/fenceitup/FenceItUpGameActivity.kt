package com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class FenceItUpGameActivity : GameGameActivity<FenceItUpGame, FenceItUpDocument, FenceItUpGameMove, FenceItUpGameState>() {
    private val document: FenceItUpDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = FenceItUpGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, FenceItUpHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        FenceItUpGame(level.layout, this, doc)
}