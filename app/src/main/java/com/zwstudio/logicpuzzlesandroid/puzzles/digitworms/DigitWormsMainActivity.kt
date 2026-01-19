package com.zwstudio.logicpuzzlesandroid.puzzles.digitworms

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class DigitWormsMainActivity : GameMainActivity<DigitWormsGame, DigitWormsDocument, DigitWormsGameMove, DigitWormsGameState>() {
    private val document: DigitWormsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, DigitWormsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, DigitWormsGameActivity::class.java))
    }
}

class DigitWormsOptionsActivity : GameOptionsActivity<DigitWormsGame, DigitWormsDocument, DigitWormsGameMove, DigitWormsGameState>() {
    private val document: DigitWormsDocument by inject()
    override val doc get() = document
}

class DigitWormsHelpActivity : GameHelpActivity<DigitWormsGame, DigitWormsDocument, DigitWormsGameMove, DigitWormsGameState>() {
    private val document: DigitWormsDocument by inject()
    override val doc get() = document
}