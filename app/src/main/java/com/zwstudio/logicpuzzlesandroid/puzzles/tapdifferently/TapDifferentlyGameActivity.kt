package com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class TapDifferentlyGameActivity : GameGameActivity<TapDifferentlyGame, TapDifferentlyDocument, TapDifferentlyGameMove, TapDifferentlyGameState>() {
    @Bean
    protected lateinit var document: TapDifferentlyDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = TapDifferentlyGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        TapDifferentlyGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        TapDifferentlyHelpActivity_.intent(this).start()
    }
}