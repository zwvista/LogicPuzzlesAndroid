package com.zwstudio.logicpuzzlesandroid.puzzles.battleships

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class BattleShipsGameActivity : GameGameActivity<BattleShipsGame, BattleShipsDocument, BattleShipsGameMove, BattleShipsGameState>() {
    @Bean
    protected lateinit var document: BattleShipsDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = BattleShipsGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        BattleShipsGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        BattleShipsHelpActivity_.intent(this).start()
    }
}
