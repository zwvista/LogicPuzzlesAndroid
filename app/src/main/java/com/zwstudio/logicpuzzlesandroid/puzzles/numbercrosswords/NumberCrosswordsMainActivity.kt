package com.zwstudio.logicpuzzlesandroid.puzzles.numbercrosswords

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class NumberCrosswordsMainActivity : GameMainActivity<NumberCrosswordsGame, NumberCrosswordsDocument, NumberCrosswordsGameMove, NumberCrosswordsGameState>() {
    private val document: NumberCrosswordsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, NumberCrosswordsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, NumberCrosswordsGameActivity::class.java))
    }
}

class NumberCrosswordsOptionsActivity : GameOptionsActivity<NumberCrosswordsGame, NumberCrosswordsDocument, NumberCrosswordsGameMove, NumberCrosswordsGameState>() {
    private val document: NumberCrosswordsDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class NumberCrosswordsHelpActivity : GameHelpActivity<NumberCrosswordsGame, NumberCrosswordsDocument, NumberCrosswordsGameMove, NumberCrosswordsGameState>() {
    private val document: NumberCrosswordsDocument by inject()
    override val doc get() = document
}