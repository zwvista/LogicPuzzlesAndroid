package com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.data.BoxItUpDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.domain.BoxItUpGame
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.domain.BoxItUpGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.domain.BoxItUpGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class BoxItUpGameActivity : GameGameActivity<BoxItUpGame, BoxItUpDocument, BoxItUpGameMove, BoxItUpGameState>() {
    @Bean
    protected lateinit var document: BoxItUpDocument
    override fun doc() = document

    @AfterViews
    override fun init() {
        gameView = BoxItUpGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = BoxItUpGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move = doc().loadMove(rec)
                game.setObject(move)
            }
            val moveIndex = doc().levelProgress().moveIndex
            if (moveIndex in 0 until game.moveCount)
                while (moveIndex != game.moveIndex)
                    game.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        BoxItUpHelpActivity_.intent(this).start()
    }
}