package com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.data.SentinelsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.domain.SentinelsGame
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class SentinelsMainActivity : GameMainActivity<SentinelsGame, SentinelsDocument, SentinelsGameMove, SentinelsGameState>() {
    @Bean
    protected lateinit var document: SentinelsDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        SentinelsOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        SentinelsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class SentinelsOptionsActivity : GameOptionsActivity<SentinelsGame, SentinelsDocument, SentinelsGameMove, SentinelsGameState>() {
    @Bean
    protected lateinit var document: SentinelsDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class SentinelsHelpActivity : GameHelpActivity<SentinelsGame, SentinelsDocument, SentinelsGameMove, SentinelsGameState>() {
    @Bean
    protected lateinit var document: SentinelsDocument
    override fun doc() = document
}