package com.zwstudio.logicpuzzlesandroid.puzzles.mineships

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class MineShipsGameActivity : GameGameActivity<MineShipsGame, MineShipsDocument, MineShipsGameMove, MineShipsGameState>() {
    @Bean
    protected lateinit var document: MineShipsDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = MineShipsGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        MineShipsGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        MineShipsHelpActivity_.intent(this).start()
    }
}