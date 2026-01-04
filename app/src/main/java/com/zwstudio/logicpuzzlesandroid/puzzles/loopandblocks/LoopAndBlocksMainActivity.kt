package com.zwstudio.logicpuzzlesandroid.puzzles.loopandblocks

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class LoopAndBlocksMainActivity : GameMainActivity<LoopAndBlocksGame, LoopAndBlocksDocument, LoopAndBlocksGameMove, LoopAndBlocksGameState>() {
    private val document: LoopAndBlocksDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, LoopAndBlocksOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, LoopAndBlocksGameActivity::class.java))
    }
}

class LoopAndBlocksOptionsActivity : GameOptionsActivity<LoopAndBlocksGame, LoopAndBlocksDocument, LoopAndBlocksGameMove, LoopAndBlocksGameState>() {
    private val document: LoopAndBlocksDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class LoopAndBlocksHelpActivity : GameHelpActivity<LoopAndBlocksGame, LoopAndBlocksDocument, LoopAndBlocksGameMove, LoopAndBlocksGameState>() {
    private val document: LoopAndBlocksDocument by inject()
    override val doc get() = document
}