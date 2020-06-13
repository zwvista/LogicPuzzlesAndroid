package com.zwstudio.logicpuzzlesandroid.puzzles.numberpath

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class NumberPathMainActivity : GameMainActivity<NumberPathGame, NumberPathDocument, NumberPathGameMove, NumberPathGameState>() {
    @Bean
    protected lateinit var document: NumberPathDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        NumberPathOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        NumberPathGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class NumberPathOptionsActivity : GameOptionsActivity<NumberPathGame, NumberPathDocument, NumberPathGameMove, NumberPathGameState>() {
    @Bean
    protected lateinit var document: NumberPathDocument
    override val doc get() = document

    protected fun onDefault() {}
}

@EActivity(R.layout.activity_game_help)
class NumberPathHelpActivity : GameHelpActivity<NumberPathGame, NumberPathDocument, NumberPathGameMove, NumberPathGameState>() {
    @Bean
    protected lateinit var document: NumberPathDocument
    override val doc get() = document
}