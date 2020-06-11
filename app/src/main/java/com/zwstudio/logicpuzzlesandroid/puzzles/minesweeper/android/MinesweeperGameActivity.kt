package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.data.MinesweeperDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGame
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class MinesweeperGameActivity : GameGameActivity<MinesweeperGame, MinesweeperDocument, MinesweeperGameMove, MinesweeperGameState>() {
    @Bean
    protected lateinit var document: MinesweeperDocument
    override fun doc() = document

    protected lateinit var gameView2: MinesweeperGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = MinesweeperGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID: String = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.setText(selectedLevelID)
        updateSolutionUI()
        levelInitilizing = true
        game = MinesweeperGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move: MinesweeperGameMove = doc().loadMove(rec)
                game.setObject(move)
            }
            val moveIndex: Int = doc().levelProgress().moveIndex
            if (moveIndex >= 0 && moveIndex < game.moveCount())
                while (moveIndex != game.moveIndex())
                    game.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        MinesweeperHelpActivity_.intent(this).start()
    }
}