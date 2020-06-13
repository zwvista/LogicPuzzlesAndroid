package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class NurikabeMainActivity : GameMainActivity<NurikabeGame, NurikabeDocument, NurikabeGameMove, NurikabeGameState>() {
    @Bean
    protected lateinit var document: NurikabeDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        NurikabeOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        NurikabeGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class NurikabeOptionsActivity : GameOptionsActivity<NurikabeGame, NurikabeDocument, NurikabeGameMove, NurikabeGameState>() {
    @Bean
    protected lateinit var document: NurikabeDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class NurikabeHelpActivity : GameHelpActivity<NurikabeGame, NurikabeDocument, NurikabeGameMove, NurikabeGameState>() {
    @Bean
    protected lateinit var document: NurikabeDocument
    override val doc get() = document
}