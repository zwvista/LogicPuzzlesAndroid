package com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class TennerGridGameActivity : GameGameActivity<TennerGridGame, TennerGridDocument, TennerGridGameMove, TennerGridGameState>() {
    @Bean
    protected lateinit var document: TennerGridDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = TennerGridGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        TennerGridGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        TennerGridHelpActivity_.intent(this).start()
    }
}