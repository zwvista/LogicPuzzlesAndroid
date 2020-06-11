package com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.data.TapDifferentlyDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain.TapDifferentlyGame
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

_
@EActivity(R.layout.activity_game_main)
class TapDifferentlyMainActivity : GameMainActivity<TapDifferentlyGame?, TapDifferentlyDocument?, TapDifferentlyGameMove?, TapDifferentlyGameState?>() {
    @Bean
    protected var document: TapDifferentlyDocument? = null
    override fun doc() = document

    @Click
    fun btnOptions() {
        TapDifferentlyOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        TapDifferentlyGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class TapDifferentlyOptionsActivity : GameOptionsActivity<TapDifferentlyGame?, TapDifferentlyDocument?, TapDifferentlyGameMove?, TapDifferentlyGameState?>() {
    @Bean
    protected var document: TapDifferentlyDocument? = null
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class TapDifferentlyHelpActivity : GameHelpActivity<TapDifferentlyGame?, TapDifferentlyDocument?, TapDifferentlyGameMove?, TapDifferentlyGameState?>() {
    @Bean
    protected var document: TapDifferentlyDocument? = null
    override fun doc() = document
}