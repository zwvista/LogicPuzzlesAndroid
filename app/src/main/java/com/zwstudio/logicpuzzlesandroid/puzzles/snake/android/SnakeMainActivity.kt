package com.zwstudio.logicpuzzlesandroid.puzzles.snake.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.snake.data.SnakeDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.snake.domain.SnakeGame
import com.zwstudio.logicpuzzlesandroid.puzzles.snake.domain.SnakeGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.snake.domain.SnakeGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class SnakeMainActivity : GameMainActivity<SnakeGame, SnakeDocument, SnakeGameMove, SnakeGameState>() {
    @Bean
    protected lateinit var document: SnakeDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        SnakeOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        SnakeGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class SnakeOptionsActivity : GameOptionsActivity<SnakeGame, SnakeDocument, SnakeGameMove, SnakeGameState>() {
    @Bean
    protected lateinit var document: SnakeDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class SnakeHelpActivity : GameHelpActivity<SnakeGame, SnakeDocument, SnakeGameMove, SnakeGameState>() {
    @Bean
    protected lateinit var document: SnakeDocument
    override fun doc() = document
}