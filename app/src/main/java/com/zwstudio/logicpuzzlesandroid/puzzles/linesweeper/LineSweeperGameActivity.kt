package com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class LineSweeperGameActivity : GameGameActivity<LineSweeperGame, LineSweeperDocument, LineSweeperGameMove, LineSweeperGameState>() {
    @Bean
    protected lateinit var document: LineSweeperDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = LineSweeperGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        LineSweeperGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        LineSweeperHelpActivity_.intent(this).start()
    }
}