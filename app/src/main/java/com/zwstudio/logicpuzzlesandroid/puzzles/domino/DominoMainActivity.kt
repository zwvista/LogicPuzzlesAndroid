package com.zwstudio.logicpuzzlesandroid.puzzles.domino

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class DominoMainActivity : GameMainActivity<DominoGame, DominoDocument, DominoGameMove, DominoGameState>() {
    private val document: DominoDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, DominoOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, DominoGameActivity::class.java))
    }
}

class DominoOptionsActivity : GameOptionsActivity<DominoGame, DominoDocument, DominoGameMove, DominoGameState>() {
    private val document: DominoDocument by inject()
    override val doc get() = document
}

class DominoHelpActivity : GameHelpActivity<DominoGame, DominoDocument, DominoGameMove, DominoGameState>() {
    private val document: DominoDocument by inject()
    override val doc get() = document
}