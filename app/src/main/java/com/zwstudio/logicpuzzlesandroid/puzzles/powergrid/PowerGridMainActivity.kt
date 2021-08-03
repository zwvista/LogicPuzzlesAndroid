package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class PowerGridMainActivity : GameMainActivity<PowerGridGame, PowerGridDocument, PowerGridGameMove, PowerGridGameState>() {
    private val document: PowerGridDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, PowerGridOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, PowerGridGameActivity::class.java))
    }
}

class PowerGridOptionsActivity : GameOptionsActivity<PowerGridGame, PowerGridDocument, PowerGridGameMove, PowerGridGameState>() {
    private val document: PowerGridDocument by inject()
    override val doc get() = document
}

class PowerGridHelpActivity : GameHelpActivity<PowerGridGame, PowerGridDocument, PowerGridGameMove, PowerGridGameState>() {
    private val document: PowerGridDocument by inject()
    override val doc get() = document
}