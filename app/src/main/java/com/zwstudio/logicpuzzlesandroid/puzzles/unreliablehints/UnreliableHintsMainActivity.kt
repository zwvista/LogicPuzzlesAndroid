package com.zwstudio.logicpuzzlesandroid.puzzles.unreliablehints

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class UnreliableHintsMainActivity : GameMainActivity<UnreliableHintsGame, UnreliableHintsDocument, UnreliableHintsGameMove, UnreliableHintsGameState>() {
    private val document: UnreliableHintsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, UnreliableHintsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, UnreliableHintsGameActivity::class.java))
    }
}

class UnreliableHintsOptionsActivity : GameOptionsActivity<UnreliableHintsGame, UnreliableHintsDocument, UnreliableHintsGameMove, UnreliableHintsGameState>() {
    private val document: UnreliableHintsDocument by inject()
    override val doc get() = document
}

class UnreliableHintsHelpActivity : GameHelpActivity<UnreliableHintsGame, UnreliableHintsDocument, UnreliableHintsGameMove, UnreliableHintsGameState>() {
    private val document: UnreliableHintsDocument by inject()
    override val doc get() = document
}
