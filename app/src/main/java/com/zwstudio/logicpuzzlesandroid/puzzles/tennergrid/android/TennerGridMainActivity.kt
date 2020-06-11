package com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.android.TennerGridGameActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.android.TennerGridOptionsActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.data.TennerGridDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain.TennerGridGame
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain.TennerGridGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain.TennerGridGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class TennerGridMainActivity : GameMainActivity<TennerGridGame, TennerGridDocument, TennerGridGameMove, TennerGridGameState>() {
    @Bean
    protected lateinit var document: TennerGridDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        TennerGridOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        TennerGridGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class TennerGridOptionsActivity : GameOptionsActivity<TennerGridGame, TennerGridDocument, TennerGridGameMove, TennerGridGameState>() {
    @Bean
    protected lateinit var document: TennerGridDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class TennerGridHelpActivity : GameHelpActivity<TennerGridGame, TennerGridDocument, TennerGridGameMove, TennerGridGameState>() {
    @Bean
    protected lateinit var document: TennerGridDocument
    override fun doc() = document
}