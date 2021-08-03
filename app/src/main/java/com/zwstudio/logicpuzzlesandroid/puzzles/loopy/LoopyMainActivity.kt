package com.zwstudio.logicpuzzlesandroid.puzzles.loopy

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class LoopyMainActivity : GameMainActivity<LoopyGame, LoopyDocument, LoopyGameMove, LoopyGameState>() {
    private val document: LoopyDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, LoopyOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, LoopyGameActivity::class.java))
    }
}

class LoopyOptionsActivity : GameOptionsActivity<LoopyGame, LoopyDocument, LoopyGameMove, LoopyGameState>() {
    private val document: LoopyDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class LoopyHelpActivity : GameHelpActivity<LoopyGame, LoopyDocument, LoopyGameMove, LoopyGameState>() {
    private val document: LoopyDocument by inject()
    override val doc get() = document
}