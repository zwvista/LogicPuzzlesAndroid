package com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class PaintTheNurikabeGameActivity : GameGameActivity<PaintTheNurikabeGame, PaintTheNurikabeDocument, PaintTheNurikabeGameMove, PaintTheNurikabeGameState>() {
    @Bean
    protected lateinit var document: PaintTheNurikabeDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = PaintTheNurikabeGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        PaintTheNurikabeGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        PaintTheNurikabeHelpActivity_.intent(this).start()
    }
}