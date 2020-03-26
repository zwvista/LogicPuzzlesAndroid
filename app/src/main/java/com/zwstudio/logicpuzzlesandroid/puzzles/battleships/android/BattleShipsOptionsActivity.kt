package com.zwstudio.logicpuzzlesandroid.puzzles.battleships.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.data.BattleShipsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain.BattleShipsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain.BattleShipsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain.BattleShipsGameState

import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_options)
open class BattleShipsOptionsActivity : GameOptionsActivity<BattleShipsGame, BattleShipsDocument, BattleShipsGameMove, BattleShipsGameState>() {
    @Bean
    protected var document: BattleShipsDocument? = null

    override fun doc(): BattleShipsDocument? {
        return document
    }
}
