package com.zwstudio.logicpuzzlesandroid.puzzles.tatamino

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class TataminoGameActivity : GameGameActivity<TataminoGame, TataminoDocument, TataminoGameMove, TataminoGameState>() {
    private val document: TataminoDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = TataminoGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, TataminoHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        TataminoGame(level.layout, this, doc)
}