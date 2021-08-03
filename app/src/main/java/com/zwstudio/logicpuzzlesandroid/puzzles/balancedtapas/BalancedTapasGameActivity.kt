package com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class BalancedTapasGameActivity : GameGameActivity<BalancedTapasGame, BalancedTapasDocument, BalancedTapasGameMove, BalancedTapasGameState>() {
    private val document: BalancedTapasDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = BalancedTapasGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, BalancedTapasHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        BalancedTapasGame(level.layout, level.settings["LeftPart"]!!, this, doc)
}
