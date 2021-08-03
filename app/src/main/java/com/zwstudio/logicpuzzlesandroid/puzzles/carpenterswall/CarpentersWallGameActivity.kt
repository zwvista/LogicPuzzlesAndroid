package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class CarpentersWallGameActivity : GameGameActivity<CarpentersWallGame, CarpentersWallDocument, CarpentersWallGameMove, CarpentersWallGameState>() {
    private val document: CarpentersWallDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = CarpentersWallGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, CarpentersWallHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        CarpentersWallGame(level.layout, this, doc)
}