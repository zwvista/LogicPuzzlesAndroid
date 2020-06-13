package com.zwstudio.logicpuzzlesandroid.puzzles.taparow

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class TapARowMainActivity : GameMainActivity<TapARowGame, TapARowDocument, TapARowGameMove, TapARowGameState>() {
    @Bean
    protected lateinit var document: TapARowDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        TapARowOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        TapARowGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class TapARowOptionsActivity : GameOptionsActivity<TapARowGame, TapARowDocument, TapARowGameMove, TapARowGameState>() {
    @Bean
    protected lateinit var document: TapARowDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class TapARowHelpActivity : GameHelpActivity<TapARowGame, TapARowDocument, TapARowGameMove, TapARowGameState>() {
    @Bean
    protected lateinit var document: TapARowDocument
    override val doc get() = document
}