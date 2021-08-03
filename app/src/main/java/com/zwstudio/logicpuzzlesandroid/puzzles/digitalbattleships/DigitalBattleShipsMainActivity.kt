package com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class DigitalBattleShipsMainActivity : GameMainActivity<DigitalBattleShipsGame, DigitalBattleShipsDocument, DigitalBattleShipsGameMove, DigitalBattleShipsGameState>() {
    private val document: DigitalBattleShipsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, DigitalBattleShipsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, DigitalBattleShipsGameActivity::class.java))
    }
}

class DigitalBattleShipsOptionsActivity : GameOptionsActivity<DigitalBattleShipsGame, DigitalBattleShipsDocument, DigitalBattleShipsGameMove, DigitalBattleShipsGameState>() {
    private val document: DigitalBattleShipsDocument by inject()
    override val doc get() = document
}

class DigitalBattleShipsHelpActivity : GameHelpActivity<DigitalBattleShipsGame, DigitalBattleShipsDocument, DigitalBattleShipsGameMove, DigitalBattleShipsGameState>() {
    private val document: DigitalBattleShipsDocument by inject()
    override val doc get() = document
}