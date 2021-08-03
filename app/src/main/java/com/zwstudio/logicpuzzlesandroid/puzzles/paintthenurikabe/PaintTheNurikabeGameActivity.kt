package com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class PaintTheNurikabeGameActivity : GameGameActivity<PaintTheNurikabeGame, PaintTheNurikabeDocument, PaintTheNurikabeGameMove, PaintTheNurikabeGameState>() {
    private val document: PaintTheNurikabeDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = PaintTheNurikabeGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, PaintTheNurikabeHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        PaintTheNurikabeGame(level.layout, this, doc)
}