package com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class CalcudokuGameActivity : GameGameActivity<CalcudokuGame, CalcudokuDocument, CalcudokuGameMove, CalcudokuGameState>() {
    @Bean
    protected lateinit var document: CalcudokuDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = CalcudokuGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        CalcudokuGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        CalcudokuHelpActivity_.intent(this).start()
    }
}