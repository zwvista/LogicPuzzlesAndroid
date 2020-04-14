package com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.data.DigitalBattleShipsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain.DigitalBattleShipsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain.DigitalBattleShipsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain.DigitalBattleShipsGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class DigitalBattleShipsMainActivity : GameMainActivity<DigitalBattleShipsGame?, DigitalBattleShipsDocument?, DigitalBattleShipsGameMove?, DigitalBattleShipsGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: DigitalBattleShipsDocument? = null
    override fun doc() = document!!

    @Click
    fun btnOptions() {
        DigitalBattleShipsOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        DigitalBattleShipsGameActivity_.intent(this).start()
    }
}