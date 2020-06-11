package com.zwstudio.logicpuzzlesandroid.puzzles.tents.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsGame
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

_
@EActivity(R.layout.activity_game_main)
class TentsMainActivity : GameMainActivity<TentsGame?, TentsDocument?, TentsGameMove?, TentsGameState?>() {
    @Bean
    protected var document: TentsDocument? = null
    override fun doc() = document

    @Click
    fun btnOptions() {
        TentsOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        TentsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class TentsOptionsActivity : GameOptionsActivity<TentsGame?, TentsDocument?, TentsGameMove?, TentsGameState?>() {
    @Bean
    protected var document: TentsDocument? = null
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class TentsHelpActivity : GameHelpActivity<TentsGame?, TentsDocument?, TentsGameMove?, TentsGameState?>() {
    @Bean
    protected var document: TentsDocument? = null
    override fun doc() = document
}