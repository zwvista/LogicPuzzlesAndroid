package com.zwstudio.logicpuzzlesandroid.puzzles.taparow

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class TapARowGameActivity : GameGameActivity<TapARowGame, TapARowDocument, TapARowGameMove, TapARowGameState>() {
    private val document: TapARowDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = TapARowGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, TapARowHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        TapARowGame(level.layout, this, doc)
}