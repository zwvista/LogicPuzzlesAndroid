package com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.data.FenceLitsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain.FenceLitsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain.FenceLitsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain.FenceLitsGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class FenceLitsMainActivity : GameMainActivity<FenceLitsGame, FenceLitsDocument, FenceLitsGameMove, FenceLitsGameState>() {
    @Bean
    protected lateinit var document: FenceLitsDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        FenceLitsOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        FenceLitsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class FenceLitsOptionsActivity : GameOptionsActivity<FenceLitsGame, FenceLitsDocument, FenceLitsGameMove, FenceLitsGameState>() {
    @Bean
    protected lateinit var document: FenceLitsDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class FenceLitsHelpActivity : GameHelpActivity<FenceLitsGame, FenceLitsDocument, FenceLitsGameMove, FenceLitsGameState>() {
    @Bean
    protected lateinit var document: FenceLitsDocument
    override fun doc() = document
}