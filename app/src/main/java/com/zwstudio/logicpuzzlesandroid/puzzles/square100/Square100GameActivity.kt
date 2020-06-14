package com.zwstudio.logicpuzzlesandroid.puzzles.square100

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class Square100GameActivity : GameGameActivity<Square100Game, Square100Document, Square100GameMove, Square100GameState>() {
    @Bean
    protected lateinit var document: Square100Document
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = Square100GameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        Square100Game(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        Square100HelpActivity_.intent(this).start()
    }
}