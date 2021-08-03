package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class TierraDelFuegoMainActivity : GameMainActivity<TierraDelFuegoGame, TierraDelFuegoDocument, TierraDelFuegoGameMove, TierraDelFuegoGameState>() {
    private val document: TierraDelFuegoDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, TierraDelFuegoOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, TierraDelFuegoGameActivity::class.java))
    }
}

class TierraDelFuegoOptionsActivity : GameOptionsActivity<TierraDelFuegoGame, TierraDelFuegoDocument, TierraDelFuegoGameMove, TierraDelFuegoGameState>() {
    private val document: TierraDelFuegoDocument by inject()
    override val doc get() = document
}

class TierraDelFuegoHelpActivity : GameHelpActivity<TierraDelFuegoGame, TierraDelFuegoDocument, TierraDelFuegoGameMove, TierraDelFuegoGameState>() {
    private val document: TierraDelFuegoDocument by inject()
    override val doc get() = document
}