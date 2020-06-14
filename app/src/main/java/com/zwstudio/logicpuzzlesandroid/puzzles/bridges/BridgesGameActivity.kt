package com.zwstudio.logicpuzzlesandroid.puzzles.bridges

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class BridgesGameActivity : GameGameActivity<BridgesGame, BridgesDocument, BridgesGameMove, BridgesGameState>() {
    @Bean
    protected lateinit var document: BridgesDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = BridgesGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        BridgesGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        BridgesHelpActivity_.intent(this).start()
    }
}