package com.zwstudio.logicpuzzlesandroid.puzzles.snake.android

import android.view.View
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.snake.data.SnakeDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.snake.domain.SnakeGame
import com.zwstudio.logicpuzzlesandroid.puzzles.snake.domain.SnakeGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.snake.domain.SnakeGameState
import fj.data.List
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class SnakeGameActivity : GameGameActivity<SnakeGame, SnakeDocument, SnakeGameMove, SnakeGameState>() {
    @Bean
    protected lateinit var document: SnakeDocument
    override fun doc() = document

    protected lateinit var gameView2: SnakeGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = SnakeGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = SnakeGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move: SnakeGameMove = doc().loadMove(rec)
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
        SnakeHelpActivity_.intent(this).start()
    }
}