package com.zwstudio.logicpuzzlesandroid.puzzles.bentbridges

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class BentBridgesGameActivity : GameGameActivity<BentBridgesGame, BentBridgesDocument, BentBridgesGameMove, BentBridgesGameState>() {
    private val document: BentBridgesDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = BentBridgesGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, BentBridgesHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        BentBridgesGame(level.layout, this, doc)
}