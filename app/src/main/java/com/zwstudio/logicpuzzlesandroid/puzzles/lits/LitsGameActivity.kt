package com.zwstudio.logicpuzzlesandroid.puzzles.lits

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class LitsGameActivity : GameGameActivity<LitsGame, LitsDocument, LitsGameMove, LitsGameState>() {
    private val document: LitsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = LitsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, LitsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        LitsGame(level.layout, this, doc)
}