package com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class TapDifferentlyGameActivity : GameGameActivity<TapDifferentlyGame, TapDifferentlyDocument, TapDifferentlyGameMove, TapDifferentlyGameState>() {
    private val document: TapDifferentlyDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = TapDifferentlyGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, TapDifferentlyHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        TapDifferentlyGame(level.layout, this, doc)
}