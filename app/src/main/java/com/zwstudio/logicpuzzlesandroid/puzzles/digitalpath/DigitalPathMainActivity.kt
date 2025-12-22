package com.zwstudio.logicpuzzlesandroid.puzzles.digitalpath

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class DigitalPathMainActivity : GameMainActivity<DigitalPathGame, DigitalPathDocument, DigitalPathGameMove, DigitalPathGameState>() {
    private val document: DigitalPathDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, DigitalPathOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, DigitalPathGameActivity::class.java))
    }
}

class DigitalPathOptionsActivity : GameOptionsActivity<DigitalPathGame, DigitalPathDocument, DigitalPathGameMove, DigitalPathGameState>() {
    private val document: DigitalPathDocument by inject()
    override val doc get() = document
}

class DigitalPathHelpActivity : GameHelpActivity<DigitalPathGame, DigitalPathDocument, DigitalPathGameMove, DigitalPathGameState>() {
    private val document: DigitalPathDocument by inject()
    override val doc get() = document
}