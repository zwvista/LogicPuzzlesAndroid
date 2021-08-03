package com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class CalcudokuGameActivity : GameGameActivity<CalcudokuGame, CalcudokuDocument, CalcudokuGameMove, CalcudokuGameState>() {
    private val document: CalcudokuDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = CalcudokuGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, CalcudokuHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        CalcudokuGame(level.layout, this, doc)
}