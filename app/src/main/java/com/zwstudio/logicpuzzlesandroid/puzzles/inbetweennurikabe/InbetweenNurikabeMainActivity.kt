package com.zwstudio.logicpuzzlesandroid.puzzles.inbetweennurikabe

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class InbetweenNurikabeMainActivity : GameMainActivity<InbetweenNurikabeGame, InbetweenNurikabeDocument, InbetweenNurikabeGameMove, InbetweenNurikabeGameState>() {
    private val document: InbetweenNurikabeDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, InbetweenNurikabeOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, InbetweenNurikabeGameActivity::class.java))
    }
}

class InbetweenNurikabeOptionsActivity : GameOptionsActivity<InbetweenNurikabeGame, InbetweenNurikabeDocument, InbetweenNurikabeGameMove, InbetweenNurikabeGameState>() {
    private val document: InbetweenNurikabeDocument by inject()
    override val doc get() = document
}

class InbetweenNurikabeHelpActivity : GameHelpActivity<InbetweenNurikabeGame, InbetweenNurikabeDocument, InbetweenNurikabeGameMove, InbetweenNurikabeGameState>() {
    private val document: InbetweenNurikabeDocument by inject()
    override val doc get() = document
}