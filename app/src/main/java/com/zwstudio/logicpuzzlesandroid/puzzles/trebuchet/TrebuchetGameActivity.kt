package com.zwstudio.logicpuzzlesandroid.puzzles.trebuchet

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class TrebuchetGameActivity : GameGameActivity<TrebuchetGame, TrebuchetDocument, TrebuchetGameMove, TrebuchetGameState>() {
    private val document: TrebuchetDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = TrebuchetGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, TrebuchetHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        TrebuchetGame(level.layout, this, doc)
}