package com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class CalcudokuMainActivity : GameMainActivity<CalcudokuGame, CalcudokuDocument, CalcudokuGameMove, CalcudokuGameState>() {
    @Bean
    protected lateinit var document: CalcudokuDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        CalcudokuOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        CalcudokuGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class CalcudokuOptionsActivity : GameOptionsActivity<CalcudokuGame, CalcudokuDocument, CalcudokuGameMove, CalcudokuGameState>() {
    @Bean
    protected lateinit var document: CalcudokuDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class CalcudokuHelpActivity : GameHelpActivity<CalcudokuGame, CalcudokuDocument, CalcudokuGameMove, CalcudokuGameState>() {
    @Bean
    protected lateinit var document: CalcudokuDocument
    override val doc get() = document
}