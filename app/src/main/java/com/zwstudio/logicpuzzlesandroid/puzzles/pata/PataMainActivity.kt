package com.zwstudio.logicpuzzlesandroid.puzzles.pata

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class PataMainActivity : GameMainActivity<PataGame, PataDocument, PataGameMove, PataGameState>() {
    private val document: PataDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, PataOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, PataGameActivity::class.java))
    }
}

class PataOptionsActivity : GameOptionsActivity<PataGame, PataDocument, PataGameMove, PataGameState>() {
    private val document: PataDocument by inject()
    override val doc get() = document
}

class PataHelpActivity : GameHelpActivity<PataGame, PataDocument, PataGameMove, PataGameState>() {
    private val document: PataDocument by inject()
    override val doc get() = document
}