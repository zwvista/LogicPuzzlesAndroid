package com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.domain.NumberLinkGame
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class NumberLinkMainActivity : GameMainActivity<NumberLinkGame, NumberLinkDocument, NumberLinkGameMove, NumberLinkGameState>() {
    @Bean
    protected lateinit var document: NumberLinkDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        NumberLinkOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        NumberLinkGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class NumberLinkOptionsActivity : GameOptionsActivity<NumberLinkGame, NumberLinkDocument, NumberLinkGameMove, NumberLinkGameState>() {
    @Bean
    protected lateinit var document: NumberLinkDocument
    override fun doc() = document

    protected fun onDefault() {}
}

@EActivity(R.layout.activity_game_help)
class NumberLinkHelpActivity : GameHelpActivity<NumberLinkGame, NumberLinkDocument, NumberLinkGameMove, NumberLinkGameState>() {
    @Bean
    protected lateinit var document: NumberLinkDocument
    override fun doc() = document
}