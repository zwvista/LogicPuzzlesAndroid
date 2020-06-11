package com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.android.PaintTheNurikabeGameActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.android.PaintTheNurikabeOptionsActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.data.PaintTheNurikabeDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domain.PaintTheNurikabeGame
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domain.PaintTheNurikabeGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domain.PaintTheNurikabeGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class PaintTheNurikabeMainActivity : GameMainActivity<PaintTheNurikabeGame?, PaintTheNurikabeDocument?, PaintTheNurikabeGameMove?, PaintTheNurikabeGameState?>() {
    @Bean
    protected var document: PaintTheNurikabeDocument? = null
    override fun doc() = document

    @Click
    fun btnOptions() {
        PaintTheNurikabeOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        PaintTheNurikabeGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class PaintTheNurikabeOptionsActivity : GameOptionsActivity<PaintTheNurikabeGame?, PaintTheNurikabeDocument?, PaintTheNurikabeGameMove?, PaintTheNurikabeGameState?>() {
    @Bean
    protected var document: PaintTheNurikabeDocument? = null
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class PaintTheNurikabeHelpActivity : GameHelpActivity<PaintTheNurikabeGame?, PaintTheNurikabeDocument?, PaintTheNurikabeGameMove?, PaintTheNurikabeGameState?>() {
    @Bean
    protected var document: PaintTheNurikabeDocument? = null
    override fun doc() = document
}