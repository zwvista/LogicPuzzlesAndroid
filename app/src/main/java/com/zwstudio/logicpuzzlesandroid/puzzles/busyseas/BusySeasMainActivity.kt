package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class BusySeasMainActivity : GameMainActivity<BusySeasGame, BusySeasDocument, BusySeasGameMove, BusySeasGameState>() {
    @Bean
    protected lateinit var document: BusySeasDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        BusySeasOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        BusySeasGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class BusySeasOptionsActivity : GameOptionsActivity<BusySeasGame, BusySeasDocument, BusySeasGameMove, BusySeasGameState>() {
    @Bean
    protected lateinit var document: BusySeasDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class BusySeasHelpActivity : GameHelpActivity<BusySeasGame, BusySeasDocument, BusySeasGameMove, BusySeasGameState>() {
    @Bean
    protected lateinit var document: BusySeasDocument
    override val doc get() = document
}