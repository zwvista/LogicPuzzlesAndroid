package com.zwstudio.logicpuzzlesandroid.puzzles.snake

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class SnakeMainActivity : GameMainActivity<SnakeGame, SnakeDocument, SnakeGameMove, SnakeGameState>() {
    @Bean
    protected lateinit var document: SnakeDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        SnakeOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        SnakeGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class SnakeOptionsActivity : GameOptionsActivity<SnakeGame, SnakeDocument, SnakeGameMove, SnakeGameState>() {
    @Bean
    protected lateinit var document: SnakeDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class SnakeHelpActivity : GameHelpActivity<SnakeGame, SnakeDocument, SnakeGameMove, SnakeGameState>() {
    @Bean
    protected lateinit var document: SnakeDocument
    override val doc get() = document
}