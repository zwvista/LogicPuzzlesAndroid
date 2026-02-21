package com.zwstudio.logicpuzzlesandroid.puzzles.crossroadblocks

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class CrossroadBlocksMainActivity : GameMainActivity<CrossroadBlocksGame, CrossroadBlocksDocument, CrossroadBlocksGameMove, CrossroadBlocksGameState>() {
    private val document: CrossroadBlocksDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, CrossroadBlocksOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, CrossroadBlocksGameActivity::class.java))
    }
}

class CrossroadBlocksOptionsActivity : GameOptionsActivity<CrossroadBlocksGame, CrossroadBlocksDocument, CrossroadBlocksGameMove, CrossroadBlocksGameState>() {
    private val document: CrossroadBlocksDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class CrossroadBlocksHelpActivity : GameHelpActivity<CrossroadBlocksGame, CrossroadBlocksDocument, CrossroadBlocksGameMove, CrossroadBlocksGameState>() {
    private val document: CrossroadBlocksDocument by inject()
    override val doc get() = document
}