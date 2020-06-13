package com.zwstudio.logicpuzzlesandroid.puzzles.domino.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.data.DominoDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.domain.DominoGame
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.domain.DominoGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.domain.DominoGameState
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

    override fun startGame() {
        val selectedLevelID = doc.selectedLevelID
        val level = doc.levels[doc.levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = DominoGame(level.layout, this, doc)
        try {
            // restore game state
            for (rec in doc.moveProgress()) {
                val move = doc.loadMove(rec)
                game.setObject(move)
            }
            val moveIndex = doc.levelProgress().moveIndex
            if (moveIndex in 0 until game.moveCount)
                while (moveIndex != game.moveIndex)
                    game.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        DominoHelpActivity_.intent(this).start()
    }
}