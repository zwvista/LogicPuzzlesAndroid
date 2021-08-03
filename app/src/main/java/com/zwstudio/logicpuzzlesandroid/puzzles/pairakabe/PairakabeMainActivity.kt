package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class PairakabeMainActivity : GameMainActivity<PairakabeGame, PairakabeDocument, PairakabeGameMove, PairakabeGameState>() {
    private val document: PairakabeDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, PairakabeOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, PairakabeGameActivity::class.java))
    }
}

class PairakabeOptionsActivity : GameOptionsActivity<PairakabeGame, PairakabeDocument, PairakabeGameMove, PairakabeGameState>() {
    private val document: PairakabeDocument by inject()
    override val doc get() = document
}

class PairakabeHelpActivity : GameHelpActivity<PairakabeGame, PairakabeDocument, PairakabeGameMove, PairakabeGameState>() {
    private val document: PairakabeDocument by inject()
    override val doc get() = document
}