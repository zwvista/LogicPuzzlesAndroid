package com.zwstudio.logicpuzzlesandroid.puzzles.boxitup

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class BoxItUpGameActivity : GameGameActivity<BoxItUpGame, BoxItUpDocument, BoxItUpGameMove, BoxItUpGameState>() {
    @Bean
    protected lateinit var document: BoxItUpDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = BoxItUpGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        BoxItUpGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        BoxItUpHelpActivity_.intent(this).start()
    }
}