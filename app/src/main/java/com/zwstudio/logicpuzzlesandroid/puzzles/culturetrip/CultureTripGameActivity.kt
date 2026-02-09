package com.zwstudio.logicpuzzlesandroid.puzzles.culturetrip

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class CultureTripGameActivity : GameGameActivity<CultureTripGame, CultureTripDocument, CultureTripGameMove, CultureTripGameState>() {
    private val document: CultureTripDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = CultureTripGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, CultureTripHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        CultureTripGame(level.layout, this, doc)
}