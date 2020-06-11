package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.data.BusySeasDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasGame
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class BusySeasMainActivity : GameMainActivity<BusySeasGame?, BusySeasDocument?, BusySeasGameMove?, BusySeasGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: BusySeasDocument? = null
    override fun doc() = document!!

    @Click
    fun btnOptions() {
        BusySeasOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        BusySeasGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class BusySeasOptionsActivity : GameOptionsActivity<BusySeasGame?, BusySeasDocument?, BusySeasGameMove?, BusySeasGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: BusySeasDocument? = null
    override fun doc() = document!!
}

@EActivity(R.layout.activity_game_help)
class BusySeasHelpActivity : GameHelpActivity<BusySeasGame?, BusySeasDocument?, BusySeasGameMove?, BusySeasGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: BusySeasDocument? = null
    override fun doc() = document!!
}