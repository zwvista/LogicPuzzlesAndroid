package com.zwstudio.logicpuzzlesandroid.puzzles.fencelits

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class FenceLitsMainActivity : GameMainActivity<FenceLitsGame, FenceLitsDocument, FenceLitsGameMove, FenceLitsGameState>() {
    private val document: FenceLitsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, FenceLitsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, FenceLitsGameActivity::class.java))
    }
}

class FenceLitsOptionsActivity : GameOptionsActivity<FenceLitsGame, FenceLitsDocument, FenceLitsGameMove, FenceLitsGameState>() {
    private val document: FenceLitsDocument by inject()
    override val doc get() = document
}

class FenceLitsHelpActivity : GameHelpActivity<FenceLitsGame, FenceLitsDocument, FenceLitsGameMove, FenceLitsGameState>() {
    private val document: FenceLitsDocument by inject()
    override val doc get() = document
}