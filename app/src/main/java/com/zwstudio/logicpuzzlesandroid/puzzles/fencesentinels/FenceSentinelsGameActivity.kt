package com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class FenceSentinelsGameActivity : GameGameActivity<FenceSentinelsGame, FenceSentinelsDocument, FenceSentinelsGameMove, FenceSentinelsGameState>() {
    @Bean
    protected lateinit var document: FenceSentinelsDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = FenceSentinelsGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        FenceSentinelsGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        FenceSentinelsHelpActivity_.intent(this).start()
    }
}