package com.zwstudio.logicpuzzlesandroid.puzzles.tatamino

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class TataminoMainActivity : GameMainActivity<TataminoGame, TataminoDocument, TataminoGameMove, TataminoGameState>() {
    private val document: TataminoDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, TataminoOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, TataminoGameActivity::class.java))
    }
}

class TataminoOptionsActivity : GameOptionsActivity<TataminoGame, TataminoDocument, TataminoGameMove, TataminoGameState>() {
    private val document: TataminoDocument by inject()
    override val doc get() = document
}

class TataminoHelpActivity : GameHelpActivity<TataminoGame, TataminoDocument, TataminoGameMove, TataminoGameState>() {
    private val document: TataminoDocument by inject()
    override val doc get() = document
}