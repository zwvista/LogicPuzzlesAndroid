package com.zwstudio.logicpuzzlesandroid.puzzles.taparow

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class TapARowGameActivity : GameGameActivity<TapARowGame, TapARowDocument, TapARowGameMove, TapARowGameState>() {
    @Bean
    protected lateinit var document: TapARowDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = TapARowGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        TapARowGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        TapARowHelpActivity_.intent(this).start()
    }
}