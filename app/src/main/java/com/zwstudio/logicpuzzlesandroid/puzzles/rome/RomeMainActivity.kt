package com.zwstudio.logicpuzzlesandroid.puzzles.rome

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class RomeMainActivity : GameMainActivity<RomeGame, RomeDocument, RomeGameMove, RomeGameState>() {
    private val document: RomeDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, RomeOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, RomeGameActivity::class.java))
    }
}

class RomeOptionsActivity : GameOptionsActivity<RomeGame, RomeDocument, RomeGameMove, RomeGameState>() {
    private val document: RomeDocument by inject()
    override val doc get() = document
}

class RomeHelpActivity : GameHelpActivity<RomeGame, RomeDocument, RomeGameMove, RomeGameState>() {
    private val document: RomeDocument by inject()
    override val doc get() = document
}