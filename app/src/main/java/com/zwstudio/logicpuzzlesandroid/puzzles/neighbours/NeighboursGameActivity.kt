package com.zwstudio.logicpuzzlesandroid.puzzles.neighbours

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class NeighboursGameActivity : GameGameActivity<NeighboursGame, NeighboursDocument, NeighboursGameMove, NeighboursGameState>() {
    @Bean
    protected lateinit var document: NeighboursDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = NeighboursGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        NeighboursGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        NeighboursHelpActivity_.intent(this).start()
    }
}