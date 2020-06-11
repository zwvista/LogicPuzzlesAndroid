package com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.data.CalcudokuDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain.CalcudokuGame
import com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain.CalcudokuGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain.CalcudokuGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class CalcudokuGameActivity : GameGameActivity<CalcudokuGame, CalcudokuDocument, CalcudokuGameMove, CalcudokuGameState>() {
    @Bean
    protected lateinit var document: CalcudokuDocument
    override fun doc() = document

    protected lateinit var gameView2: CalcudokuGameView
    override fun getGameView() = gameView2

    @AfterViews
    override fun init() {
        gameView2 = CalcudokuGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = CalcudokuGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move = doc().loadMove(rec)
                game.setObject(move)
            }
            val moveIndex = doc().levelProgress().moveIndex
            if (moveIndex >= 0 && moveIndex < game.moveCount())
                while (moveIndex != game.moveIndex())
                    game.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        CalcudokuHelpActivity_.intent(this).start()
    }
}