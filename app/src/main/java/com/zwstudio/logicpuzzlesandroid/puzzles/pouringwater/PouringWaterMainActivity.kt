package com.zwstudio.logicpuzzlesandroid.puzzles.pouringwater

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class PouringWaterMainActivity : GameMainActivity<PouringWaterGame, PouringWaterDocument, PouringWaterGameMove, PouringWaterGameState>() {
    private val document: PouringWaterDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, PouringWaterOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, PouringWaterGameActivity::class.java))
    }
}

class PouringWaterOptionsActivity : GameOptionsActivity<PouringWaterGame, PouringWaterDocument, PouringWaterGameMove, PouringWaterGameState>() {
    private val document: PouringWaterDocument by inject()
    override val doc get() = document
}

class PouringWaterHelpActivity : GameHelpActivity<PouringWaterGame, PouringWaterDocument, PouringWaterGameMove, PouringWaterGameState>() {
    private val document: PouringWaterDocument by inject()
    override val doc get() = document
}