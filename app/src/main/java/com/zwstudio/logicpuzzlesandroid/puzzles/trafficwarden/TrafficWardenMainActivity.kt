package com.zwstudio.logicpuzzlesandroid.puzzles.trafficwarden

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class TrafficWardenMainActivity : GameMainActivity<TrafficWardenGame, TrafficWardenDocument, TrafficWardenGameMove, TrafficWardenGameState>() {
    private val document: TrafficWardenDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, TrafficWardenOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, TrafficWardenGameActivity::class.java))
    }
}

class TrafficWardenOptionsActivity : GameOptionsActivity<TrafficWardenGame, TrafficWardenDocument, TrafficWardenGameMove, TrafficWardenGameState>() {
    private val document: TrafficWardenDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class TrafficWardenHelpActivity : GameHelpActivity<TrafficWardenGame, TrafficWardenDocument, TrafficWardenGameMove, TrafficWardenGameState>() {
    private val document: TrafficWardenDocument by inject()
    override val doc get() = document
}