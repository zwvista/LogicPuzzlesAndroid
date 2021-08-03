package com.zwstudio.logicpuzzlesandroid.puzzles.mineships

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class MineShipsMainActivity : GameMainActivity<MineShipsGame, MineShipsDocument, MineShipsGameMove, MineShipsGameState>() {
    private val document: MineShipsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, MineShipsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, MineShipsGameActivity::class.java))
    }
}

class MineShipsOptionsActivity : GameOptionsActivity<MineShipsGame, MineShipsDocument, MineShipsGameMove, MineShipsGameState>() {
    private val document: MineShipsDocument by inject()
    override val doc get() = document
}

class MineShipsHelpActivity : GameHelpActivity<MineShipsGame, MineShipsDocument, MineShipsGameMove, MineShipsGameState>() {
    private val document: MineShipsDocument by inject()
    override val doc get() = document
}