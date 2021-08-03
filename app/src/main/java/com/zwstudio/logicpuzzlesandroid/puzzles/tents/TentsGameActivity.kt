package com.zwstudio.logicpuzzlesandroid.puzzles.tents

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class TentsGameActivity : GameGameActivity<TentsGame, TentsDocument, TentsGameMove, TentsGameState>() {
    private val document: TentsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = TentsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, TentsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        TentsGame(level.layout, this, doc)
}