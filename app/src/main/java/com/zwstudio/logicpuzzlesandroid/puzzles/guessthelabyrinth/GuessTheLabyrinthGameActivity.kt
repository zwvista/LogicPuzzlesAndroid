package com.zwstudio.logicpuzzlesandroid.puzzles.guessthelabyrinth

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class GuessTheLabyrinthGameActivity : GameGameActivity<GuessTheLabyrinthGame, GuessTheLabyrinthDocument, GuessTheLabyrinthGameMove, GuessTheLabyrinthGameState>() {
    private val document: GuessTheLabyrinthDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = GuessTheLabyrinthGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, GuessTheLabyrinthHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        GuessTheLabyrinthGame(level.layout, this, doc)
}