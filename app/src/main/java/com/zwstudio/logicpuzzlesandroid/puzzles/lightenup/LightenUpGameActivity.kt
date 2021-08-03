package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class LightenUpGameActivity : GameGameActivity<LightenUpGame, LightenUpDocument, LightenUpGameMove, LightenUpGameState>() {
    private val document: LightenUpDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = LightenUpGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, LightenUpHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        LightenUpGame(level.layout, this, doc)
}