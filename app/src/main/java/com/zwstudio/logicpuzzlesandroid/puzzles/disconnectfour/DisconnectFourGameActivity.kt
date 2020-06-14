package com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class DisconnectFourGameActivity : GameGameActivity<DisconnectFourGame, DisconnectFourDocument, DisconnectFourGameMove, DisconnectFourGameState>() {
    @Bean
    protected lateinit var document: DisconnectFourDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = DisconnectFourGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        DisconnectFourGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        DisconnectFourHelpActivity_.intent(this).start()
    }
}