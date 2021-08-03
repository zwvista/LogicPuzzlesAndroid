package com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class BalancedTapasMainActivity : GameMainActivity<BalancedTapasGame, BalancedTapasDocument, BalancedTapasGameMove, BalancedTapasGameState>() {
    private val document: BalancedTapasDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, BalancedTapasOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, BalancedTapasGameActivity::class.java))
    }
}

class BalancedTapasOptionsActivity : GameOptionsActivity<BalancedTapasGame, BalancedTapasDocument, BalancedTapasGameMove, BalancedTapasGameState>() {
    private val document: BalancedTapasDocument by inject()
    override val doc get() = document
}

class BalancedTapasHelpActivity : GameHelpActivity<BalancedTapasGame, BalancedTapasDocument, BalancedTapasGameMove, BalancedTapasGameState>() {
    private val document: BalancedTapasDocument by inject()
    override val doc get() = document
}
