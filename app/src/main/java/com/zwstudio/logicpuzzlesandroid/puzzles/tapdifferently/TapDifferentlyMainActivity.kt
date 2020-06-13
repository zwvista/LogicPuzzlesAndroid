package com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class TapDifferentlyMainActivity : GameMainActivity<TapDifferentlyGame, TapDifferentlyDocument, TapDifferentlyGameMove, TapDifferentlyGameState>() {
    @Bean
    protected lateinit var document: TapDifferentlyDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        TapDifferentlyOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        TapDifferentlyGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class TapDifferentlyOptionsActivity : GameOptionsActivity<TapDifferentlyGame, TapDifferentlyDocument, TapDifferentlyGameMove, TapDifferentlyGameState>() {
    @Bean
    protected lateinit var document: TapDifferentlyDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class TapDifferentlyHelpActivity : GameHelpActivity<TapDifferentlyGame, TapDifferentlyDocument, TapDifferentlyGameMove, TapDifferentlyGameState>() {
    @Bean
    protected lateinit var document: TapDifferentlyDocument
    override val doc get() = document
}