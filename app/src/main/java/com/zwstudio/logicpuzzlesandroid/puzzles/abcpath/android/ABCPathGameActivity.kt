package com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.data.ABCPathDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain.ABCPathGame
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain.ABCPathGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain.ABCPathGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class ABCPathGameActivity : GameGameActivity<ABCPathGame, ABCPathDocument, ABCPathGameMove, ABCPathGameState>() {
    @Bean
    protected lateinit var document: ABCPathDocument
    override fun doc() = document

    @AfterViews
    override fun init() {
        gameView = ABCPathGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()

        levelInitilizing = true
        game = ABCPathGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move = doc().loadMove(rec)
                game.setObject(move)
            }
            val moveIndex = doc().levelProgress().moveIndex
            if (moveIndex >= 0 && moveIndex < game.moveCount())
                while (moveIndex != game.moveIndex())
                    game.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        ABCPathHelpActivity_.intent(this).start()
    }
}
