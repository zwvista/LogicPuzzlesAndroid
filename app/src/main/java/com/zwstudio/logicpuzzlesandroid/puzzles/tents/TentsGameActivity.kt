package com.zwstudio.logicpuzzlesandroid.puzzles.tents

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class TentsGameActivity : GameGameActivity<TentsGame, TentsDocument, TentsGameMove, TentsGameState>() {
    @Bean
    protected lateinit var document: TentsDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = TentsGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        TentsGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        TentsHelpActivity_.intent(this).start()
    }
}