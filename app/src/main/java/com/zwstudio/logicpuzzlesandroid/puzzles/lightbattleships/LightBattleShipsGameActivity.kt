package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class LightBattleShipsGameActivity : GameGameActivity<LightBattleShipsGame, LightBattleShipsDocument, LightBattleShipsGameMove, LightBattleShipsGameState>() {
    @Bean
    protected lateinit var document: LightBattleShipsDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = LightBattleShipsGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        LightBattleShipsGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        LightBattleShipsHelpActivity_.intent(this).start()
    }
}