package com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.data.CalcudokuDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain.CalcudokuGame
import com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain.CalcudokuGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain.CalcudokuGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_options)
class CalcudokuOptionsActivity : GameOptionsActivity<CalcudokuGame?, CalcudokuDocument?, CalcudokuGameMove?, CalcudokuGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: CalcudokuDocument? = null
    override fun doc() = document!!
}