package com.zwstudio.logicpuzzlesandroid.puzzles.crossroadsx

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class CrossroadsXGameActivity : GameGameActivity<CrossroadsXGame, CrossroadsXDocument, CrossroadsXGameMove, CrossroadsXGameState>() {
    private val document: CrossroadsXDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = CrossroadsXGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, CrossroadsXHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel): CrossroadsXGame {
        val sum = (level.settings["sum"] ?: "10").toInt()
        return CrossroadsXGame(level.layout, sum, this, doc)
    }
}