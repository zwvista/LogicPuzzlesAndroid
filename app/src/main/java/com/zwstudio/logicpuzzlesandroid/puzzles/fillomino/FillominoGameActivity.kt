package com.zwstudio.logicpuzzlesandroid.puzzles.fillomino

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class FillominoGameActivity : GameGameActivity<FillominoGame, FillominoDocument, FillominoGameMove, FillominoGameState>() {
    @Bean
    protected lateinit var document: FillominoDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = FillominoGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        FillominoGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        FillominoHelpActivity_.intent(this).start()
    }
}