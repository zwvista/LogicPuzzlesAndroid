package com.zwstudio.logicpuzzlesandroid.puzzles.newcarpentersquare

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class NewCarpenterSquareGameActivity : GameGameActivity<NewCarpenterSquareGame, NewCarpenterSquareDocument, NewCarpenterSquareGameMove, NewCarpenterSquareGameState>() {
    private val document: NewCarpenterSquareDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = NewCarpenterSquareGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, NewCarpenterSquareHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        NewCarpenterSquareGame(level.layout, this, doc)
}