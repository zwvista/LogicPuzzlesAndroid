package com.zwstudio.logicpuzzlesandroid.puzzles.inbetweennurikabe

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class InbetweenNurikabeGameActivity : GameGameActivity<InbetweenNurikabeGame, InbetweenNurikabeDocument, InbetweenNurikabeGameMove, InbetweenNurikabeGameState>() {
    private val document: InbetweenNurikabeDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = InbetweenNurikabeGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, InbetweenNurikabeHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        InbetweenNurikabeGame(level.layout, this, doc)
}