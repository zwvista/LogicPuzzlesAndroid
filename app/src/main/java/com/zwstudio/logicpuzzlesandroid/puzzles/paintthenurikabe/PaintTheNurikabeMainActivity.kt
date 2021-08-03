package com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class PaintTheNurikabeMainActivity : GameMainActivity<PaintTheNurikabeGame, PaintTheNurikabeDocument, PaintTheNurikabeGameMove, PaintTheNurikabeGameState>() {
    private val document: PaintTheNurikabeDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, PaintTheNurikabeOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, PaintTheNurikabeGameActivity::class.java))
    }
}

class PaintTheNurikabeOptionsActivity : GameOptionsActivity<PaintTheNurikabeGame, PaintTheNurikabeDocument, PaintTheNurikabeGameMove, PaintTheNurikabeGameState>() {
    private val document: PaintTheNurikabeDocument by inject()
    override val doc get() = document
}

class PaintTheNurikabeHelpActivity : GameHelpActivity<PaintTheNurikabeGame, PaintTheNurikabeDocument, PaintTheNurikabeGameMove, PaintTheNurikabeGameState>() {
    private val document: PaintTheNurikabeDocument by inject()
    override val doc get() = document
}