package com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.data.NeighboursDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGame
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class NeighboursGameActivity : GameGameActivity<NeighboursGame, NeighboursDocument, NeighboursGameMove, NeighboursGameState>() {
    @Bean
    protected lateinit var document: NeighboursDocument
    override fun doc() = document

    protected lateinit var gameView2: NeighboursGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = NeighboursGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = NeighboursGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move: NeighboursGameMove = doc().loadMove(rec)
                game.setObject(move)
            }
            val moveIndex: Int = doc().levelProgress().moveIndex
            if (moveIndex >= 0 && moveIndex < game.moveCount())
                while (moveIndex != game.moveIndex())
                    game.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        NeighboursHelpActivity_.intent(this).start()
    }
}