package com.zwstudio.logicpuzzlesandroid.puzzles.sentinels

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class SentinelsMainActivity : GameMainActivity<SentinelsGame, SentinelsDocument, SentinelsGameMove, SentinelsGameState>() {
    @Bean
    protected lateinit var document: SentinelsDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        SentinelsOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        SentinelsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class SentinelsOptionsActivity : GameOptionsActivity<SentinelsGame, SentinelsDocument, SentinelsGameMove, SentinelsGameState>() {
    @Bean
    protected lateinit var document: SentinelsDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class SentinelsHelpActivity : GameHelpActivity<SentinelsGame, SentinelsDocument, SentinelsGameMove, SentinelsGameState>() {
    @Bean
    protected lateinit var document: SentinelsDocument
    override val doc get() = document
}