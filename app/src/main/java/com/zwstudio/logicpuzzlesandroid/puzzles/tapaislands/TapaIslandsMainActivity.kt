package com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class TapaIslandsMainActivity : GameMainActivity<TapaIslandsGame, TapaIslandsDocument, TapaIslandsGameMove, TapaIslandsGameState>() {
    private val document: TapaIslandsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, TapaIslandsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, TapaIslandsGameActivity::class.java))
    }
}

class TapaIslandsOptionsActivity : GameOptionsActivity<TapaIslandsGame, TapaIslandsDocument, TapaIslandsGameMove, TapaIslandsGameState>() {
    private val document: TapaIslandsDocument by inject()
    override val doc get() = document
}

class TapaIslandsHelpActivity : GameHelpActivity<TapaIslandsGame, TapaIslandsDocument, TapaIslandsGameMove, TapaIslandsGameState>() {
    private val document: TapaIslandsDocument by inject()
    override val doc get() = document
}