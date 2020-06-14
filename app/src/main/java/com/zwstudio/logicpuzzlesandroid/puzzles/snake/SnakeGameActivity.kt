package com.zwstudio.logicpuzzlesandroid.puzzles.snake

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class SnakeGameActivity : GameGameActivity<SnakeGame, SnakeDocument, SnakeGameMove, SnakeGameState>() {
    @Bean
    protected lateinit var document: SnakeDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = SnakeGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        SnakeGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        SnakeHelpActivity_.intent(this).start()
    }
}