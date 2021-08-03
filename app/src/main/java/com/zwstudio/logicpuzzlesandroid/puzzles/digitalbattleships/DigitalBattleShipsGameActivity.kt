package com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class DigitalBattleShipsGameActivity : GameGameActivity<DigitalBattleShipsGame, DigitalBattleShipsDocument, DigitalBattleShipsGameMove, DigitalBattleShipsGameState>() {
    private val document: DigitalBattleShipsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = DigitalBattleShipsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, DigitalBattleShipsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        DigitalBattleShipsGame(level.layout, this, doc)
}