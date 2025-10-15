package com.zwstudio.logicpuzzlesandroid.puzzles.turntwice

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class TurnTwiceMainActivity : GameMainActivity<TurnTwiceGame, TurnTwiceDocument, TurnTwiceGameMove, TurnTwiceGameState>() {
    private val document: TurnTwiceDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, TurnTwiceOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, TurnTwiceGameActivity::class.java))
    }
}

class TurnTwiceOptionsActivity : GameOptionsActivity<TurnTwiceGame, TurnTwiceDocument, TurnTwiceGameMove, TurnTwiceGameState>() {
    private val document: TurnTwiceDocument by inject()
    override val doc get() = document
}

class TurnTwiceHelpActivity : GameHelpActivity<TurnTwiceGame, TurnTwiceDocument, TurnTwiceGameMove, TurnTwiceGameState>() {
    private val document: TurnTwiceDocument by inject()
    override val doc get() = document
}