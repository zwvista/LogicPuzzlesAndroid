package com.zwstudio.logicpuzzlesandroid.puzzles.liarliar

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class LiarLiarGameActivity : GameGameActivity<LiarLiarGame, LiarLiarDocument, LiarLiarGameMove, LiarLiarGameState>() {
    private val document: LiarLiarDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = LiarLiarGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, LiarLiarHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        LiarLiarGame(level.layout, this, doc)
}