package com.zwstudio.logicpuzzlesandroid.puzzles.floweromino

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class FlowerOMinoMainActivity : GameMainActivity<FlowerOMinoGame, FlowerOMinoDocument, FlowerOMinoGameMove, FlowerOMinoGameState>() {
    private val document: FlowerOMinoDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, FlowerOMinoOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, FlowerOMinoGameActivity::class.java))
    }
}

class FlowerOMinoOptionsActivity : GameOptionsActivity<FlowerOMinoGame, FlowerOMinoDocument, FlowerOMinoGameMove, FlowerOMinoGameState>() {
    private val document: FlowerOMinoDocument by inject()
    override val doc get() = document
}

class FlowerOMinoHelpActivity : GameHelpActivity<FlowerOMinoGame, FlowerOMinoDocument, FlowerOMinoGameMove, FlowerOMinoGameState>() {
    private val document: FlowerOMinoDocument by inject()
    override val doc get() = document
}
