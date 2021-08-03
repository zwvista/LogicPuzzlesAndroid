package com.zwstudio.logicpuzzlesandroid.puzzles.tapa

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class TapaMainActivity : GameMainActivity<TapaGame, TapaDocument, TapaGameMove, TapaGameState>() {
    private val document: TapaDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, TapaOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, TapaGameActivity::class.java))
    }
}

class TapaOptionsActivity : GameOptionsActivity<TapaGame, TapaDocument, TapaGameMove, TapaGameState>() {
    private val document: TapaDocument by inject()
    override val doc get() = document
}

class TapaHelpActivity : GameHelpActivity<TapaGame, TapaDocument, TapaGameMove, TapaGameState>() {
    private val document: TapaDocument by inject()
    override val doc get() = document
}