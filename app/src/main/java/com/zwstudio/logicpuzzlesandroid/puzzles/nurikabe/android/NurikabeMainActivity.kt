package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

_
@EActivity(R.layout.activity_game_main)
class NurikabeMainActivity : GameMainActivity<NurikabeGame?, NurikabeDocument?, NurikabeGameMove?, NurikabeGameState?>() {
    @Bean
    protected var document: NurikabeDocument? = null
    override fun doc() = document

    @Click
    fun btnOptions() {
        NurikabeOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        NurikabeGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class NurikabeOptionsActivity : GameOptionsActivity<NurikabeGame?, NurikabeDocument?, NurikabeGameMove?, NurikabeGameState?>() {
    @Bean
    protected var document: NurikabeDocument? = null
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class NurikabeHelpActivity : GameHelpActivity<NurikabeGame?, NurikabeDocument?, NurikabeGameMove?, NurikabeGameState?>() {
    @Bean
    protected var document: NurikabeDocument? = null
    override fun doc() = document
}