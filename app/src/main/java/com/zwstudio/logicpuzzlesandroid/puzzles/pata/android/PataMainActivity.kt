package com.zwstudio.logicpuzzlesandroid.puzzles.pata.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.data.PataDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain.PataGame
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain.PataGameMove
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

_
@EActivity(R.layout.activity_game_main)
class PataMainActivity : GameMainActivity<PataGame?, PataDocument?, PataGameMove?, PataGameState?>() {
    @Bean
    protected var document: PataDocument? = null
    override fun doc() = document

    @Click
    fun btnOptions() {
        PataOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        PataGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class PataOptionsActivity : GameOptionsActivity<PataGame?, PataDocument?, PataGameMove?, PataGameState?>() {
    @Bean
    protected var document: PataDocument? = null
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class PataHelpActivity : GameHelpActivity<PataGame?, PataDocument?, PataGameMove?, PataGameState?>() {
    @Bean
    protected var document: PataDocument? = null
    override fun doc() = document
}