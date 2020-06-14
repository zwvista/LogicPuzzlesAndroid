package com.zwstudio.logicpuzzlesandroid.puzzles.sentinels

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class SentinelsGameActivity : GameGameActivity<SentinelsGame, SentinelsDocument, SentinelsGameMove, SentinelsGameState>() {
    @Bean
    protected lateinit var document: SentinelsDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = SentinelsGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        SentinelsGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        SentinelsHelpActivity_.intent(this).start()
    }
}