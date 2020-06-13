package com.zwstudio.logicpuzzlesandroid.puzzles.numberlink

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class NumberLinkMainActivity : GameMainActivity<NumberLinkGame, NumberLinkDocument, NumberLinkGameMove, NumberLinkGameState>() {
    @Bean
    protected lateinit var document: NumberLinkDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        NumberLinkOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        NumberLinkGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class NumberLinkOptionsActivity : GameOptionsActivity<NumberLinkGame, NumberLinkDocument, NumberLinkGameMove, NumberLinkGameState>() {
    @Bean
    protected lateinit var document: NumberLinkDocument
    override val doc get() = document

    protected fun onDefault() {}
}

@EActivity(R.layout.activity_game_help)
class NumberLinkHelpActivity : GameHelpActivity<NumberLinkGame, NumberLinkDocument, NumberLinkGameMove, NumberLinkGameState>() {
    @Bean
    protected lateinit var document: NumberLinkDocument
    override val doc get() = document
}