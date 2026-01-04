package com.zwstudio.logicpuzzlesandroid.puzzles.straightandturn

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class StraightAndTurnMainActivity : GameMainActivity<StraightAndTurnGame, StraightAndTurnDocument, StraightAndTurnGameMove, StraightAndTurnGameState>() {
    private val document: StraightAndTurnDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, StraightAndTurnOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, StraightAndTurnGameActivity::class.java))
    }
}

class StraightAndTurnOptionsActivity : GameOptionsActivity<StraightAndTurnGame, StraightAndTurnDocument, StraightAndTurnGameMove, StraightAndTurnGameState>() {
    private val document: StraightAndTurnDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class StraightAndTurnHelpActivity : GameHelpActivity<StraightAndTurnGame, StraightAndTurnDocument, StraightAndTurnGameMove, StraightAndTurnGameState>() {
    private val document: StraightAndTurnDocument by inject()
    override val doc get() = document
}