package com.zwstudio.logicpuzzlesandroid.puzzles.numberpath

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class NumberPathMainActivity : GameMainActivity<NumberPathGame, NumberPathDocument, NumberPathGameMove, NumberPathGameState>() {
    private val document: NumberPathDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, NumberPathOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, NumberPathGameActivity::class.java))
    }
}

class NumberPathOptionsActivity : GameOptionsActivity<NumberPathGame, NumberPathDocument, NumberPathGameMove, NumberPathGameState>() {
    private val document: NumberPathDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class NumberPathHelpActivity : GameHelpActivity<NumberPathGame, NumberPathDocument, NumberPathGameMove, NumberPathGameState>() {
    private val document: NumberPathDocument by inject()
    override val doc get() = document
}