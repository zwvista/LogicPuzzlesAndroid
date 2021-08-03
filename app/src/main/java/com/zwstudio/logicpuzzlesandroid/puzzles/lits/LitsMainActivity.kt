package com.zwstudio.logicpuzzlesandroid.puzzles.lits

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class LitsMainActivity : GameMainActivity<LitsGame, LitsDocument, LitsGameMove, LitsGameState>() {
    private val document: LitsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, LitsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, LitsGameActivity::class.java))
    }
}

class LitsOptionsActivity : GameOptionsActivity<LitsGame, LitsDocument, LitsGameMove, LitsGameState>() {
    private val document: LitsDocument by inject()
    override val doc get() = document
}

class LitsHelpActivity : GameHelpActivity<LitsGame, LitsDocument, LitsGameMove, LitsGameState>() {
    private val document: LitsDocument by inject()
    override val doc get() = document
}