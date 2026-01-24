package com.zwstudio.logicpuzzlesandroid.puzzles.youturnmeon

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class YouTurnMeOnMainActivity : GameMainActivity<YouTurnMeOnGame, YouTurnMeOnDocument, YouTurnMeOnGameMove, YouTurnMeOnGameState>() {
    private val document: YouTurnMeOnDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, YouTurnMeOnOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, YouTurnMeOnGameActivity::class.java))
    }
}

class YouTurnMeOnOptionsActivity : GameOptionsActivity<YouTurnMeOnGame, YouTurnMeOnDocument, YouTurnMeOnGameMove, YouTurnMeOnGameState>() {
    private val document: YouTurnMeOnDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class YouTurnMeOnHelpActivity : GameHelpActivity<YouTurnMeOnGame, YouTurnMeOnDocument, YouTurnMeOnGameMove, YouTurnMeOnGameState>() {
    private val document: YouTurnMeOnDocument by inject()
    override val doc get() = document
}