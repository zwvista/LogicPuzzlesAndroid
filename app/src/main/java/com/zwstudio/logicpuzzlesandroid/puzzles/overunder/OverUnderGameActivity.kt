package com.zwstudio.logicpuzzlesandroid.puzzles.overunder

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class OverUnderGameActivity : GameGameActivity<OverUnderGame, OverUnderDocument, OverUnderGameMove, OverUnderGameState>() {
    @Bean
    protected lateinit var document: OverUnderDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = OverUnderGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        OverUnderGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        OverUnderHelpActivity_.intent(this).start()
    }
}