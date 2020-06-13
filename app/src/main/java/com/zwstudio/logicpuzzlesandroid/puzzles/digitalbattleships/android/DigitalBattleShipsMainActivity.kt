package com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.data.DigitalBattleShipsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain.DigitalBattleShipsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain.DigitalBattleShipsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain.DigitalBattleShipsGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class DigitalBattleShipsMainActivity : GameMainActivity<DigitalBattleShipsGame, DigitalBattleShipsDocument, DigitalBattleShipsGameMove, DigitalBattleShipsGameState>() {
    @Bean
    protected lateinit var document: DigitalBattleShipsDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        DigitalBattleShipsOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        DigitalBattleShipsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class DigitalBattleShipsOptionsActivity : GameOptionsActivity<DigitalBattleShipsGame, DigitalBattleShipsDocument, DigitalBattleShipsGameMove, DigitalBattleShipsGameState>() {
    @Bean
    protected lateinit var document: DigitalBattleShipsDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class DigitalBattleShipsHelpActivity : GameHelpActivity<DigitalBattleShipsGame, DigitalBattleShipsDocument, DigitalBattleShipsGameMove, DigitalBattleShipsGameState>() {
    @Bean
    protected lateinit var document: DigitalBattleShipsDocument
    override val doc get() = document
}