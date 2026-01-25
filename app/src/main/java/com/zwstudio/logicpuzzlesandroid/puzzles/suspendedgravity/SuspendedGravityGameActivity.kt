package com.zwstudio.logicpuzzlesandroid.puzzles.suspendedgravity

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class SuspendedGravityGameActivity : GameGameActivity<SuspendedGravityGame, SuspendedGravityDocument, SuspendedGravityGameMove, SuspendedGravityGameState>() {
    private val document: SuspendedGravityDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = SuspendedGravityGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, SuspendedGravityHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        SuspendedGravityGame(level.layout, this, doc)
}