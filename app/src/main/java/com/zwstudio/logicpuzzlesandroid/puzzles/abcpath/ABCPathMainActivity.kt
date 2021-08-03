package com.zwstudio.logicpuzzlesandroid.puzzles.abcpath

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class ABCPathMainActivity : GameMainActivity<ABCPathGame, ABCPathDocument, ABCPathGameMove, ABCPathGameState>() {
    private val document: ABCPathDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, ABCPathOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, ABCPathGameActivity::class.java))
    }
}

class ABCPathOptionsActivity : GameOptionsActivity<ABCPathGame, ABCPathDocument, ABCPathGameMove, ABCPathGameState>() {
    private val document: ABCPathDocument by inject()
    override val doc get() = document
}

class ABCPathHelpActivity : GameHelpActivity<ABCPathGame, ABCPathDocument, ABCPathGameMove, ABCPathGameState>() {
    private val document: ABCPathDocument by inject()
    override val doc get() = document
}
