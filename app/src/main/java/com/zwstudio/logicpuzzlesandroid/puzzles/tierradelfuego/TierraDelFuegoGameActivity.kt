package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class TierraDelFuegoGameActivity : GameGameActivity<TierraDelFuegoGame, TierraDelFuegoDocument, TierraDelFuegoGameMove, TierraDelFuegoGameState>() {
    @Bean
    protected lateinit var document: TierraDelFuegoDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = TierraDelFuegoGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        TierraDelFuegoGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        TierraDelFuegoHelpActivity_.intent(this).start()
    }
}