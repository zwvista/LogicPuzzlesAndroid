package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class LightBattleShipsGameActivity : GameGameActivity<LightBattleShipsGame, LightBattleShipsDocument, LightBattleShipsGameMove, LightBattleShipsGameState>() {
    private val document: LightBattleShipsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = LightBattleShipsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, LightBattleShipsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        LightBattleShipsGame(level.layout, this, doc)
}