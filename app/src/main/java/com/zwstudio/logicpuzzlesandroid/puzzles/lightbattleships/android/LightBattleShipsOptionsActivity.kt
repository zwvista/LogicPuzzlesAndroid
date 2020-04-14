package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.data.LightBattleShipsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_options)
class LightBattleShipsOptionsActivity : GameOptionsActivity<LightBattleShipsGame?, LightBattleShipsDocument?, LightBattleShipsGameMove?, LightBattleShipsGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: LightBattleShipsDocument? = null
    override fun doc() = document!!
}