package com.zwstudio.logicpuzzlesandroid.puzzles.domino

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class DominoGameActivity : GameGameActivity<DominoGame, DominoDocument, DominoGameMove, DominoGameState>() {
    @Bean
    protected lateinit var document: DominoDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = DominoGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        DominoGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        DominoHelpActivity_.intent(this).start()
    }
}