package com.zwstudio.logicpuzzlesandroid.puzzles.mirrors

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class MirrorsGameActivity : GameGameActivity<MirrorsGame, MirrorsDocument, MirrorsGameMove, MirrorsGameState>() {
    private val document: MirrorsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = MirrorsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, MirrorsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        MirrorsGame(level.layout, this, doc)
}