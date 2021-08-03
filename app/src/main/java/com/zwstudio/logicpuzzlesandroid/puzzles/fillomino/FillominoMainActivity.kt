package com.zwstudio.logicpuzzlesandroid.puzzles.fillomino

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class FillominoMainActivity : GameMainActivity<FillominoGame, FillominoDocument, FillominoGameMove, FillominoGameState>() {
    private val document: FillominoDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, FillominoOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, FillominoGameActivity::class.java))
    }
}

class FillominoOptionsActivity : GameOptionsActivity<FillominoGame, FillominoDocument, FillominoGameMove, FillominoGameState>() {
    private val document: FillominoDocument by inject()
    override val doc get() = document
}

class FillominoHelpActivity : GameHelpActivity<FillominoGame, FillominoDocument, FillominoGameMove, FillominoGameState>() {
    private val document: FillominoDocument by inject()
    override val doc get() = document
}