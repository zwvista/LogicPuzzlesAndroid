package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class WallSentinelsGameActivity : GameGameActivity<WallSentinelsGame, WallSentinelsDocument, WallSentinelsGameMove, WallSentinelsGameState>() {
    @Bean
    protected lateinit var document: WallSentinelsDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = WallSentinelsGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        WallSentinelsGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        WallSentinelsHelpActivity_.intent(this).start()
    }
}
