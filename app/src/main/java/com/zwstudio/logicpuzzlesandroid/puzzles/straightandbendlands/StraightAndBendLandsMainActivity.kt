package com.zwstudio.logicpuzzlesandroid.puzzles.straightandbendlands

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class StraightAndBendLandsMainActivity : GameMainActivity<StraightAndBendLandsGame, StraightAndBendLandsDocument, StraightAndBendLandsGameMove, StraightAndBendLandsGameState>() {
    private val document: StraightAndBendLandsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, StraightAndBendLandsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, StraightAndBendLandsGameActivity::class.java))
    }
}

class StraightAndBendLandsOptionsActivity : GameOptionsActivity<StraightAndBendLandsGame, StraightAndBendLandsDocument, StraightAndBendLandsGameMove, StraightAndBendLandsGameState>() {
    private val document: StraightAndBendLandsDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class StraightAndBendLandsHelpActivity : GameHelpActivity<StraightAndBendLandsGame, StraightAndBendLandsDocument, StraightAndBendLandsGameMove, StraightAndBendLandsGameState>() {
    private val document: StraightAndBendLandsDocument by inject()
    override val doc get() = document
}