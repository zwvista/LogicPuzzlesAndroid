package com.zwstudio.logicpuzzlesandroid.puzzles.battleships.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.data.BattleShipsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain.BattleShipsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain.BattleShipsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain.BattleShipsGameState

import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
open class BattleShipsMainActivity : GameMainActivity<BattleShipsGame, BattleShipsDocument, BattleShipsGameMove, BattleShipsGameState>() {
    @Bean
    protected var document: BattleShipsDocument? = null

    override fun doc(): BattleShipsDocument? {
        return document
    }

    @Click
    internal fun btnOptions() {
        BattleShipsOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc()!!.resumeGame()
        BattleShipsGameActivity_.intent(this).start()
    }
}
