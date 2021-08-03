package com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class DisconnectFourGameActivity : GameGameActivity<DisconnectFourGame, DisconnectFourDocument, DisconnectFourGameMove, DisconnectFourGameState>() {
    private val document: DisconnectFourDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = DisconnectFourGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, DisconnectFourHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        DisconnectFourGame(level.layout, this, doc)
}