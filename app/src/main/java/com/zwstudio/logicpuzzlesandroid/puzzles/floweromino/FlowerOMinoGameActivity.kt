package com.zwstudio.logicpuzzlesandroid.puzzles.floweromino

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class FlowerOMinoGameActivity : GameGameActivity<FlowerOMinoGame, FlowerOMinoDocument, FlowerOMinoGameMove, FlowerOMinoGameState>() {
    private val document: FlowerOMinoDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = FlowerOMinoGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, FlowerOMinoHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        FlowerOMinoGame(level.layout, this, doc)
}
