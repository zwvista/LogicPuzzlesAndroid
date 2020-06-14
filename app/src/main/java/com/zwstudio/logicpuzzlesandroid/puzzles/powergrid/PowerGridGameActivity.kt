package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class PowerGridGameActivity : GameGameActivity<PowerGridGame, PowerGridDocument, PowerGridGameMove, PowerGridGameState>() {
    @Bean
    protected lateinit var document: PowerGridDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = PowerGridGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        PowerGridGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        PowerGridHelpActivity_.intent(this).start()
    }
}