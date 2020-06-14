package com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class RippleEffectGameActivity : GameGameActivity<RippleEffectGame, RippleEffectDocument, RippleEffectGameMove, RippleEffectGameState>() {
    @Bean
    protected lateinit var document: RippleEffectDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = RippleEffectGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        RippleEffectGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        RippleEffectHelpActivity_.intent(this).start()
    }
}