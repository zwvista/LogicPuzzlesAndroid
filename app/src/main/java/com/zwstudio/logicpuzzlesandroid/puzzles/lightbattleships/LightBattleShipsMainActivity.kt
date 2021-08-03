package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class LightBattleShipsMainActivity : GameMainActivity<LightBattleShipsGame, LightBattleShipsDocument, LightBattleShipsGameMove, LightBattleShipsGameState>() {
    private val document: LightBattleShipsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, LightBattleShipsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, LightBattleShipsGameActivity::class.java))
    }
}

class LightBattleShipsOptionsActivity : GameOptionsActivity<LightBattleShipsGame, LightBattleShipsDocument, LightBattleShipsGameMove, LightBattleShipsGameState>() {
    private val document: LightBattleShipsDocument by inject()
    override val doc get() = document
}

class LightBattleShipsHelpActivity : GameHelpActivity<LightBattleShipsGame, LightBattleShipsDocument, LightBattleShipsGameMove, LightBattleShipsGameState>() {
    private val document: LightBattleShipsDocument by inject()
    override val doc get() = document
}