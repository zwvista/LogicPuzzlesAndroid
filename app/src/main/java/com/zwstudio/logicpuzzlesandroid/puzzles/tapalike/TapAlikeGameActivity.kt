package com.zwstudio.logicpuzzlesandroid.puzzles.tapalike

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class TapAlikeGameActivity : GameGameActivity<TapAlikeGame, TapAlikeDocument, TapAlikeGameMove, TapAlikeGameState>() {
    @Bean
    protected lateinit var document: TapAlikeDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = TapAlikeGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        TapAlikeGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        TapAlikeHelpActivity_.intent(this).start()
    }
}