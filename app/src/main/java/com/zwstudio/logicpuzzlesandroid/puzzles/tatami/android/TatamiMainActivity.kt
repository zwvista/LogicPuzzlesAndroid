package com.zwstudio.logicpuzzlesandroid.puzzles.tatami.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.data.TatamiDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.domain.TatamiGame
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.domain.TatamiGameMove
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

_
@EActivity(R.layout.activity_game_main)
class TatamiMainActivity : GameMainActivity<TatamiGame?, TatamiDocument?, TatamiGameMove?, TatamiGameState?>() {
    @Bean
    protected var document: TatamiDocument? = null
    override fun doc() = document

    @Click
    fun btnOptions() {
        TatamiOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        TatamiGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class TatamiOptionsActivity : GameOptionsActivity<TatamiGame?, TatamiDocument?, TatamiGameMove?, TatamiGameState?>() {
    @Bean
    protected var document: TatamiDocument? = null
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class TatamiHelpActivity : GameHelpActivity<TatamiGame?, TatamiDocument?, TatamiGameMove?, TatamiGameState?>() {
    @Bean
    protected var document: TatamiDocument? = null
    override fun doc() = document
}