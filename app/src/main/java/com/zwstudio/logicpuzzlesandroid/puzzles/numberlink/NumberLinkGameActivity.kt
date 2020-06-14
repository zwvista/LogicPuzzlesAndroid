package com.zwstudio.logicpuzzlesandroid.puzzles.numberlink

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class NumberLinkGameActivity : GameGameActivity<NumberLinkGame, NumberLinkDocument, NumberLinkGameMove, NumberLinkGameState>() {
    @Bean
    protected lateinit var document: NumberLinkDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = NumberLinkGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        NumberLinkGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        NumberLinkHelpActivity_.intent(this).start()
    }
}