package com.zwstudio.logicpuzzlesandroid.puzzles.pipemania

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class PipemaniaGameActivity : GameGameActivity<PipemaniaGame, PipemaniaDocument, PipemaniaGameMove, PipemaniaGameState>() {
    private val document: PipemaniaDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = PipemaniaGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, PipemaniaHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        PipemaniaGame(level.layout, this, doc)
}