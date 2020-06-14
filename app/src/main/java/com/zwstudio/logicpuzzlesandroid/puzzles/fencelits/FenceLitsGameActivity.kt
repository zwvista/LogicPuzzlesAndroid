package com.zwstudio.logicpuzzlesandroid.puzzles.fencelits

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class FenceLitsGameActivity : GameGameActivity<FenceLitsGame, FenceLitsDocument, FenceLitsGameMove, FenceLitsGameState>() {
    @Bean
    protected lateinit var document: FenceLitsDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = FenceLitsGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        FenceLitsGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        FenceLitsHelpActivity_.intent(this).start()
    }
}