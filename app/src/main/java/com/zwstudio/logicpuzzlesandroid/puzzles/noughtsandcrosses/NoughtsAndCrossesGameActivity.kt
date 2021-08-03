package com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class NoughtsAndCrossesGameActivity : GameGameActivity<NoughtsAndCrossesGame, NoughtsAndCrossesDocument, NoughtsAndCrossesGameMove, NoughtsAndCrossesGameState>() {
    private val document: NoughtsAndCrossesDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = NoughtsAndCrossesGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, NoughtsAndCrossesHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        NoughtsAndCrossesGame(level.layout, level.settings["num"]!![0], this, doc)
}