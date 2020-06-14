package com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class BalancedTapasGameActivity : GameGameActivity<BalancedTapasGame, BalancedTapasDocument, BalancedTapasGameMove, BalancedTapasGameState>() {
    @Bean
    protected lateinit var document: BalancedTapasDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = BalancedTapasGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        BalancedTapasGame(level.layout, level.settings["LeftPart"]!!, this, doc)

    @Click
    protected fun btnHelp() {
        BalancedTapasHelpActivity_.intent(this).start()
    }
}
