package com.zwstudio.logicpuzzlesandroid.puzzles.turnmeup

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class TurnMeUpMainActivity : GameMainActivity<TurnMeUpGame, TurnMeUpDocument, TurnMeUpGameMove, TurnMeUpGameState>() {
    private val document: TurnMeUpDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, TurnMeUpOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, TurnMeUpGameActivity::class.java))
    }
}

class TurnMeUpOptionsActivity : GameOptionsActivity<TurnMeUpGame, TurnMeUpDocument, TurnMeUpGameMove, TurnMeUpGameState>() {
    private val document: TurnMeUpDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class TurnMeUpHelpActivity : GameHelpActivity<TurnMeUpGame, TurnMeUpDocument, TurnMeUpGameMove, TurnMeUpGameState>() {
    private val document: TurnMeUpDocument by inject()
    override val doc get() = document
}