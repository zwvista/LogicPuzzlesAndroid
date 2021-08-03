package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class BusySeasMainActivity : GameMainActivity<BusySeasGame, BusySeasDocument, BusySeasGameMove, BusySeasGameState>() {
    private val document: BusySeasDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, BusySeasOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, BusySeasGameActivity::class.java))
    }
}

class BusySeasOptionsActivity : GameOptionsActivity<BusySeasGame, BusySeasDocument, BusySeasGameMove, BusySeasGameState>() {
    private val document: BusySeasDocument by inject()
    override val doc get() = document
}

class BusySeasHelpActivity : GameHelpActivity<BusySeasGame, BusySeasDocument, BusySeasGameMove, BusySeasGameState>() {
    private val document: BusySeasDocument by inject()
    override val doc get() = document
}