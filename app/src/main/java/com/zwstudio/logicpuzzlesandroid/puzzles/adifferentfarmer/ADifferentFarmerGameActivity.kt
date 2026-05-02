package com.zwstudio.logicpuzzlesandroid.puzzles.adifferentfarmer

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class ADifferentFarmerGameActivity : GameGameActivity<ADifferentFarmerGame, ADifferentFarmerDocument, ADifferentFarmerGameMove, ADifferentFarmerGameState>() {
    private val document: ADifferentFarmerDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = ADifferentFarmerGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, ADifferentFarmerHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        ADifferentFarmerGame(level.layout, this, doc)
}