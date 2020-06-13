package com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.data.TapAlikeDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.domain.TapAlikeGame
import com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.domain.TapAlikeGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.domain.TapAlikeGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class TapAlikeMainActivity : GameMainActivity<TapAlikeGame, TapAlikeDocument, TapAlikeGameMove, TapAlikeGameState>() {
    @Bean
    protected lateinit var document: TapAlikeDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        TapAlikeOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        TapAlikeGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class TapAlikeOptionsActivity : GameOptionsActivity<TapAlikeGame, TapAlikeDocument, TapAlikeGameMove, TapAlikeGameState>() {
    @Bean
    protected lateinit var document: TapAlikeDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class TapAlikeHelpActivity : GameHelpActivity<TapAlikeGame, TapAlikeDocument, TapAlikeGameMove, TapAlikeGameState>() {
    @Bean
    protected lateinit var document: TapAlikeDocument
    override val doc get() = document
}