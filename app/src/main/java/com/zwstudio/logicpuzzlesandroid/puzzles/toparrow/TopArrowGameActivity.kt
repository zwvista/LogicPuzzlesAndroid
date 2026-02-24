package com.zwstudio.logicpuzzlesandroid.puzzles.toparrow

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class TopArrowGameActivity : GameGameActivity<TopArrowGame, TopArrowDocument, TopArrowGameMove, TopArrowGameState>() {
    private val document: TopArrowDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = TopArrowGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, TopArrowHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        TopArrowGame(level.layout, this, doc)
}