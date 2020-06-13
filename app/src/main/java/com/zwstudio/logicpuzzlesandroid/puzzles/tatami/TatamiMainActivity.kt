package com.zwstudio.logicpuzzlesandroid.puzzles.tatami

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class TatamiMainActivity : GameMainActivity<TatamiGame, TatamiDocument, TatamiGameMove, TatamiGameState>() {
    @Bean
    protected lateinit var document: TatamiDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        TatamiOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        TatamiGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class TatamiOptionsActivity : GameOptionsActivity<TatamiGame, TatamiDocument, TatamiGameMove, TatamiGameState>() {
    @Bean
    protected lateinit var document: TatamiDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class TatamiHelpActivity : GameHelpActivity<TatamiGame, TatamiDocument, TatamiGameMove, TatamiGameState>() {
    @Bean
    protected lateinit var document: TatamiDocument
    override val doc get() = document
}