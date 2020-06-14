package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class MinesweeperGameActivity : GameGameActivity<MinesweeperGame, MinesweeperDocument, MinesweeperGameMove, MinesweeperGameState>() {
    @Bean
    protected lateinit var document: MinesweeperDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = MinesweeperGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        MinesweeperGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        MinesweeperHelpActivity_.intent(this).start()
    }
}