package com.zwstudio.logicpuzzlesandroid.puzzles.loopandblocks

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class LoopAndBlocksGameActivity : GameGameActivity<LoopAndBlocksGame, LoopAndBlocksDocument, LoopAndBlocksGameMove, LoopAndBlocksGameState>() {
    private val document: LoopAndBlocksDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = LoopAndBlocksGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, LoopAndBlocksHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        LoopAndBlocksGame(level.layout, this, doc)
}