package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class LightBattleShipsMainActivity : GameMainActivity<LightBattleShipsGame, LightBattleShipsDocument, LightBattleShipsGameMove, LightBattleShipsGameState>() {
    @Bean
    protected lateinit var document: LightBattleShipsDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        LightBattleShipsOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        LightBattleShipsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class LightBattleShipsOptionsActivity : GameOptionsActivity<LightBattleShipsGame, LightBattleShipsDocument, LightBattleShipsGameMove, LightBattleShipsGameState>() {
    @Bean
    protected lateinit var document: LightBattleShipsDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class LightBattleShipsHelpActivity : GameHelpActivity<LightBattleShipsGame, LightBattleShipsDocument, LightBattleShipsGameMove, LightBattleShipsGameState>() {
    @Bean
    protected lateinit var document: LightBattleShipsDocument
    override val doc get() = document
}