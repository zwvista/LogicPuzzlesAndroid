package com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.android

import android.view.View
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.data.LineSweeperDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperGame
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperGameState
import fj.data.List
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class LineSweeperGameActivity : GameGameActivity<LineSweeperGame?, LineSweeperDocument?, LineSweeperGameMove?, LineSweeperGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: LineSweeperDocument? = null
    override fun doc() = document!!

    protected var gameView: LineSweeperGameView? = null
    override fun getGameView() = gameView!!

    @AfterViews
    override fun init() {
        gameView = LineSweeperGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[List.iterableList(doc().levels).toStream().indexOf { o: GameLevel -> o.id == selectedLevelID }.orSome(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = LineSweeperGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move = doc().loadMove(rec)
                game!!.setObject(move)
            }
            val moveIndex = doc().levelProgress().moveIndex
            if (moveIndex >= 0 && moveIndex < game!!.moveCount()) while (moveIndex != game!!.moveIndex()) game!!.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        LineSweeperHelpActivity_.intent(this).start()
    }
}