package com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class TennerGridMainActivity : GameMainActivity<TennerGridGame, TennerGridDocument, TennerGridGameMove, TennerGridGameState>() {
    @Bean
    protected lateinit var document: TennerGridDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        TennerGridOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        TennerGridGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class TennerGridOptionsActivity : GameOptionsActivity<TennerGridGame, TennerGridDocument, TennerGridGameMove, TennerGridGameState>() {
    @Bean
    protected lateinit var document: TennerGridDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class TennerGridHelpActivity : GameHelpActivity<TennerGridGame, TennerGridDocument, TennerGridGameMove, TennerGridGameState>() {
    @Bean
    protected lateinit var document: TennerGridDocument
    override val doc get() = document
}