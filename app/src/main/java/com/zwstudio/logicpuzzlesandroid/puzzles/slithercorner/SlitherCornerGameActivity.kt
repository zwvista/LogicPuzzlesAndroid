package com.zwstudio.logicpuzzlesandroid.puzzles.slithercorner

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class SlitherCornerGameActivity : GameGameActivity<SlitherCornerGame, SlitherCornerDocument, SlitherCornerGameMove, SlitherCornerGameState>() {
    private val document: SlitherCornerDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = SlitherCornerGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, SlitherCornerHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        SlitherCornerGame(level.layout, this, doc)
}