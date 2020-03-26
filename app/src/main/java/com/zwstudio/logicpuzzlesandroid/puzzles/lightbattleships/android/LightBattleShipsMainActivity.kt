package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.data.LightBattleShipsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
open class LightBattleShipsMainActivity : GameMainActivity<LightBattleShipsGame?, LightBattleShipsDocument?, LightBattleShipsGameMove?, LightBattleShipsGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: LightBattleShipsDocument? = null
    override fun doc(): LightBattleShipsDocument {
        return document!!
    }

    @Click
    fun btnOptions() {
        LightBattleShipsOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        LightBattleShipsGameActivity_.intent(this).start()
    }
}