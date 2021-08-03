package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class BusySeasGameActivity : GameGameActivity<BusySeasGame, BusySeasDocument, BusySeasGameMove, BusySeasGameState>() {
    private val document: BusySeasDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = BusySeasGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, BusySeasHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        BusySeasGame(level.layout, this, doc)
}