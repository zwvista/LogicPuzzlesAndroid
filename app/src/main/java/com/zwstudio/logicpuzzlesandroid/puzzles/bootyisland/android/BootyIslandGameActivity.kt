package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.android

import android.view.View

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.data.BootyIslandDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandGame
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandGameState

import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

import fj.data.List.iterableList

@EActivity(R.layout.activity_game_game)
open class BootyIslandGameActivity : GameGameActivity<BootyIslandGame, BootyIslandDocument, BootyIslandGameMove, BootyIslandGameState>() {
    @Bean
    protected var document: BootyIslandDocument? = null

    protected var gameView: BootyIslandGameView
    override fun doc(): BootyIslandDocument? {
        return document
    }

    override fun getGameView(): View? {
        return gameView
    }

    @AfterViews
    override fun init() {
        gameView = BootyIslandGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc()!!.selectedLevelID
        val level = doc()!!.levels[iterableList(doc()!!.levels).toStream().indexOf { o -> o.id == selectedLevelID }.orSome(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()

        levelInitilizing = true
        game = BootyIslandGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc()!!.moveProgress()) {
                val move = doc()!!.loadMove(rec)
                game.setObject(move)
            }
            val moveIndex = doc()!!.levelProgress().moveIndex
            if (moveIndex >= 0 && moveIndex < game.moveCount())
                while (moveIndex != game.moveIndex())
                    game.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        BootyIslandHelpActivity_.intent(this).start()
    }
}
