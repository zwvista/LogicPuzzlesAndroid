package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class CarpentersSquareGameActivity : GameGameActivity<CarpentersSquareGame, CarpentersSquareDocument, CarpentersSquareGameMove, CarpentersSquareGameState>() {
    private val document: CarpentersSquareDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = CarpentersSquareGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, CarpentersSquareHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        CarpentersSquareGame(level.layout, this, doc)
}