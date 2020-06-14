package com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class SumscrapersGameActivity : GameGameActivity<SumscrapersGame, SumscrapersDocument, SumscrapersGameMove, SumscrapersGameState>() {
    @Bean
    protected lateinit var document: SumscrapersDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = SumscrapersGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        SumscrapersGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        SumscrapersHelpActivity_.intent(this).start()
    }
}