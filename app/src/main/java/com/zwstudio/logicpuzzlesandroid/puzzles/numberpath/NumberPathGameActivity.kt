package com.zwstudio.logicpuzzlesandroid.puzzles.numberpath

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class NumberPathGameActivity : GameGameActivity<NumberPathGame, NumberPathDocument, NumberPathGameMove, NumberPathGameState>() {
    @Bean
    protected lateinit var document: NumberPathDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = NumberPathGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        NumberPathGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        NumberPathHelpActivity_.intent(this).start()
    }
}