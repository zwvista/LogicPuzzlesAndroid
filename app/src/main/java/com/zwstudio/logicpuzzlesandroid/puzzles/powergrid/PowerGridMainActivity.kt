package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class PowerGridMainActivity : GameMainActivity<PowerGridGame, PowerGridDocument, PowerGridGameMove, PowerGridGameState>() {
    @Bean
    protected lateinit var document: PowerGridDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        PowerGridOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        PowerGridGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class PowerGridOptionsActivity : GameOptionsActivity<PowerGridGame, PowerGridDocument, PowerGridGameMove, PowerGridGameState>() {
    @Bean
    protected lateinit var document: PowerGridDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class PowerGridHelpActivity : GameHelpActivity<PowerGridGame, PowerGridDocument, PowerGridGameMove, PowerGridGameState>() {
    @Bean
    protected lateinit var document: PowerGridDocument
    override val doc get() = document
}