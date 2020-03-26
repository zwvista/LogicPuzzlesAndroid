package com.zwstudio.logicpuzzlesandroid.puzzles.battleships.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
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
    protected lateinit var document: BattleShipsDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        BattleShipsOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        BattleShipsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
open class BattleShipsOptionsActivity : GameOptionsActivity<BattleShipsGame, BattleShipsDocument, BattleShipsGameMove, BattleShipsGameState>() {
    @Bean
    protected lateinit var document: BattleShipsDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
open class BattleShipsHelpActivity : GameHelpActivity<BattleShipsGame, BattleShipsDocument, BattleShipsGameMove, BattleShipsGameState>() {
    @Bean
    protected lateinit var document: BattleShipsDocument
    override fun doc() = document
}
