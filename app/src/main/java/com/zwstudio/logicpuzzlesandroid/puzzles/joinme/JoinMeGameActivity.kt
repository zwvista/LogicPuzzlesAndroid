package com.zwstudio.logicpuzzlesandroid.puzzles.joinme

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class JoinMeGameActivity : GameGameActivity<JoinMeGame, JoinMeDocument, JoinMeGameMove, JoinMeGameState>() {
    private val document: JoinMeDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = JoinMeGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, JoinMeHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel): JoinMeGame {
        val stitches = (level.settings["Stitches"] ?: "1").toInt()
        return JoinMeGame(level.layout, stitches, this, doc)
    }
}