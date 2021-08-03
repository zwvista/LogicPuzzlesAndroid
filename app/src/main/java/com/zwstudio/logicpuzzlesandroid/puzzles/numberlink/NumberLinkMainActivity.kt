package com.zwstudio.logicpuzzlesandroid.puzzles.numberlink

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class NumberLinkMainActivity : GameMainActivity<NumberLinkGame, NumberLinkDocument, NumberLinkGameMove, NumberLinkGameState>() {
    private val document: NumberLinkDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, NumberLinkOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, NumberLinkGameActivity::class.java))
    }
}

class NumberLinkOptionsActivity : GameOptionsActivity<NumberLinkGame, NumberLinkDocument, NumberLinkGameMove, NumberLinkGameState>() {
    private val document: NumberLinkDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class NumberLinkHelpActivity : GameHelpActivity<NumberLinkGame, NumberLinkDocument, NumberLinkGameMove, NumberLinkGameState>() {
    private val document: NumberLinkDocument by inject()
    override val doc get() = document
}