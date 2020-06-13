package com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class PaintTheNurikabeMainActivity : GameMainActivity<PaintTheNurikabeGame, PaintTheNurikabeDocument, PaintTheNurikabeGameMove, PaintTheNurikabeGameState>() {
    @Bean
    protected lateinit var document: PaintTheNurikabeDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        PaintTheNurikabeOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        PaintTheNurikabeGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class PaintTheNurikabeOptionsActivity : GameOptionsActivity<PaintTheNurikabeGame, PaintTheNurikabeDocument, PaintTheNurikabeGameMove, PaintTheNurikabeGameState>() {
    @Bean
    protected lateinit var document: PaintTheNurikabeDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class PaintTheNurikabeHelpActivity : GameHelpActivity<PaintTheNurikabeGame, PaintTheNurikabeDocument, PaintTheNurikabeGameMove, PaintTheNurikabeGameState>() {
    @Bean
    protected lateinit var document: PaintTheNurikabeDocument
    override val doc get() = document
}