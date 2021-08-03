package com.zwstudio.logicpuzzlesandroid.puzzles.galaxies

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class GalaxiesGameActivity : GameGameActivity<GalaxiesGame, GalaxiesDocument, GalaxiesGameMove, GalaxiesGameState>() {
    private val document: GalaxiesDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = GalaxiesGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, GalaxiesHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        GalaxiesGame(level.layout, this, doc)
}