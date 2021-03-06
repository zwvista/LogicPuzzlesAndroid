package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class PairakabeMainActivity : GameMainActivity<PairakabeGame, PairakabeDocument, PairakabeGameMove, PairakabeGameState>() {
    @Bean
    protected lateinit var document: PairakabeDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        PairakabeOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        PairakabeGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class PairakabeOptionsActivity : GameOptionsActivity<PairakabeGame, PairakabeDocument, PairakabeGameMove, PairakabeGameState>() {
    @Bean
    protected lateinit var document: PairakabeDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class PairakabeHelpActivity : GameHelpActivity<PairakabeGame, PairakabeDocument, PairakabeGameMove, PairakabeGameState>() {
    @Bean
    protected lateinit var document: PairakabeDocument
    override val doc get() = document
}