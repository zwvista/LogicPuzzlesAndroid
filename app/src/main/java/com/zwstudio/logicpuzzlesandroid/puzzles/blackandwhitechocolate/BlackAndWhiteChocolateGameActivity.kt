package com.zwstudio.logicpuzzlesandroid.puzzles.blackandwhitechocolate

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class BlackAndWhiteChocolateGameActivity : GameGameActivity<BlackAndWhiteChocolateGame, BlackAndWhiteChocolateDocument, BlackAndWhiteChocolateGameMove, BlackAndWhiteChocolateGameState>() {
    private val document: BlackAndWhiteChocolateDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = BlackAndWhiteChocolateGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, BlackAndWhiteChocolateHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        BlackAndWhiteChocolateGame(level.layout, this, doc)
}