package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class NurikabeGameActivity : GameGameActivity<NurikabeGame, NurikabeDocument, NurikabeGameMove, NurikabeGameState>() {
    private val document: NurikabeDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = NurikabeGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, NurikabeHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        NurikabeGame(level.layout, this, doc)
}