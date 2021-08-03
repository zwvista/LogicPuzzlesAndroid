package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class WallSentinelsMainActivity : GameMainActivity<WallSentinelsGame, WallSentinelsDocument, WallSentinelsGameMove, WallSentinelsGameState>() {
    private val document: WallSentinelsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, WallSentinelsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, WallSentinelsGameActivity::class.java))
    }
}

class WallSentinelsOptionsActivity : GameOptionsActivity<WallSentinelsGame, WallSentinelsDocument, WallSentinelsGameMove, WallSentinelsGameState>() {
    private val document: WallSentinelsDocument by inject()
    override val doc get() = document
}

class WallSentinelsHelpActivity : GameHelpActivity<WallSentinelsGame, WallSentinelsDocument, WallSentinelsGameMove, WallSentinelsGameState>() {
    private val document: WallSentinelsDocument by inject()
    override val doc get() = document
}
