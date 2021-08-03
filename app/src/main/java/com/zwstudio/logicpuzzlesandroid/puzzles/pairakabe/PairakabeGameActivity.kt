package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class PairakabeGameActivity : GameGameActivity<PairakabeGame, PairakabeDocument, PairakabeGameMove, PairakabeGameState>() {
    private val document: PairakabeDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = PairakabeGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, PairakabeHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        PairakabeGame(level.layout, this, doc)
}