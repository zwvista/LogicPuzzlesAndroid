package com.zwstudio.logicpuzzlesandroid.puzzles.tetrominopegs

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class TetrominoPegsGameActivity : GameGameActivity<TetrominoPegsGame, TetrominoPegsDocument, TetrominoPegsGameMove, TetrominoPegsGameState>() {
    private val document: TetrominoPegsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = TetrominoPegsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, TetrominoPegsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        TetrominoPegsGame(level.layout, this, doc)
}