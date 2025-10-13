package com.zwstudio.logicpuzzlesandroid.puzzles.arrows

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class ArrowsMainActivity : GameMainActivity<ArrowsGame, ArrowsDocument, ArrowsGameMove, ArrowsGameState>() {
    private val document: ArrowsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, ArrowsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, ArrowsGameActivity::class.java))
    }
}

class ArrowsOptionsActivity : GameOptionsActivity<ArrowsGame, ArrowsDocument, ArrowsGameMove, ArrowsGameState>() {
    private val document: ArrowsDocument by inject()
    override val doc get() = document
}

class ArrowsHelpActivity : GameHelpActivity<ArrowsGame, ArrowsDocument, ArrowsGameMove, ArrowsGameState>() {
    private val document: ArrowsDocument by inject()
    override val doc get() = document
}
