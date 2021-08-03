package com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class CalcudokuMainActivity : GameMainActivity<CalcudokuGame, CalcudokuDocument, CalcudokuGameMove, CalcudokuGameState>() {
    private val document: CalcudokuDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, CalcudokuOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, CalcudokuGameActivity::class.java))
    }
}

class CalcudokuOptionsActivity : GameOptionsActivity<CalcudokuGame, CalcudokuDocument, CalcudokuGameMove, CalcudokuGameState>() {
    private val document: CalcudokuDocument by inject()
    override val doc get() = document
}

class CalcudokuHelpActivity : GameHelpActivity<CalcudokuGame, CalcudokuDocument, CalcudokuGameMove, CalcudokuGameState>() {
    private val document: CalcudokuDocument by inject()
    override val doc get() = document
}