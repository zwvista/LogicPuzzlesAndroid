package com.zwstudio.logicpuzzlesandroid.puzzles.tatami

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class TatamiGameActivity : GameGameActivity<TatamiGame, TatamiDocument, TatamiGameMove, TatamiGameState>() {
    private val document: TatamiDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = TatamiGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, TatamiHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        TatamiGame(level.layout, this, doc)
}