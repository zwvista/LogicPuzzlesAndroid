package com.zwstudio.logicpuzzlesandroid.puzzles.numbercrossing

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class NumberCrossingMainActivity : GameMainActivity<NumberCrossingGame, NumberCrossingDocument, NumberCrossingGameMove, NumberCrossingGameState>() {
    private val document: NumberCrossingDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, NumberCrossingOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, NumberCrossingGameActivity::class.java))
    }
}

class NumberCrossingOptionsActivity : GameOptionsActivity<NumberCrossingGame, NumberCrossingDocument, NumberCrossingGameMove, NumberCrossingGameState>() {
    private val document: NumberCrossingDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class NumberCrossingHelpActivity : GameHelpActivity<NumberCrossingGame, NumberCrossingDocument, NumberCrossingGameMove, NumberCrossingGameState>() {
    private val document: NumberCrossingDocument by inject()
    override val doc get() = document
}