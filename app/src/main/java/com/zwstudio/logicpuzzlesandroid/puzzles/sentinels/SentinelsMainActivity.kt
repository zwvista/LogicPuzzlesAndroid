package com.zwstudio.logicpuzzlesandroid.puzzles.sentinels

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class SentinelsMainActivity : GameMainActivity<SentinelsGame, SentinelsDocument, SentinelsGameMove, SentinelsGameState>() {
    private val document: SentinelsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, SentinelsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, SentinelsGameActivity::class.java))
    }
}

class SentinelsOptionsActivity : GameOptionsActivity<SentinelsGame, SentinelsDocument, SentinelsGameMove, SentinelsGameState>() {
    private val document: SentinelsDocument by inject()
    override val doc get() = document
}

class SentinelsHelpActivity : GameHelpActivity<SentinelsGame, SentinelsDocument, SentinelsGameMove, SentinelsGameState>() {
    private val document: SentinelsDocument by inject()
    override val doc get() = document
}