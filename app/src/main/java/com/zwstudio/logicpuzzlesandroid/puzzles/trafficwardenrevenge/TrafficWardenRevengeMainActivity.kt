package com.zwstudio.logicpuzzlesandroid.puzzles.trafficwardenrevenge

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class TrafficWardenRevengeMainActivity : GameMainActivity<TrafficWardenRevengeGame, TrafficWardenRevengeDocument, TrafficWardenRevengeGameMove, TrafficWardenRevengeGameState>() {
    private val document: TrafficWardenRevengeDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, TrafficWardenRevengeOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, TrafficWardenRevengeGameActivity::class.java))
    }
}

class TrafficWardenRevengeOptionsActivity : GameOptionsActivity<TrafficWardenRevengeGame, TrafficWardenRevengeDocument, TrafficWardenRevengeGameMove, TrafficWardenRevengeGameState>() {
    private val document: TrafficWardenRevengeDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class TrafficWardenRevengeHelpActivity : GameHelpActivity<TrafficWardenRevengeGame, TrafficWardenRevengeDocument, TrafficWardenRevengeGameMove, TrafficWardenRevengeGameState>() {
    private val document: TrafficWardenRevengeDocument by inject()
    override val doc get() = document
}