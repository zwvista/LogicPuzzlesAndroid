package com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.data.BalancedTapasDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasGame
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasGameState

import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
open class BalancedTapasMainActivity : GameMainActivity<BalancedTapasGame, BalancedTapasDocument, BalancedTapasGameMove, BalancedTapasGameState>() {
    @Bean
    protected lateinit var document: BalancedTapasDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        BalancedTapasOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        BalancedTapasGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
open class BalancedTapasOptionsActivity : GameOptionsActivity<BalancedTapasGame, BalancedTapasDocument, BalancedTapasGameMove, BalancedTapasGameState>() {
    @Bean
    protected lateinit var document: BalancedTapasDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
open class BalancedTapasHelpActivity : GameHelpActivity<BalancedTapasGame, BalancedTapasDocument, BalancedTapasGameMove, BalancedTapasGameState>() {
    @Bean
    protected lateinit var document: BalancedTapasDocument
    override fun doc() = document
}
