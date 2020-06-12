package com.zwstudio.logicpuzzlesandroid.puzzles.square100.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.square100.data.Square100Document
import com.zwstudio.logicpuzzlesandroid.puzzles.square100.domain.Square100Game
import com.zwstudio.logicpuzzlesandroid.puzzles.square100.domain.Square100GameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.square100.domain.Square100GameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class Square100GameActivity : GameGameActivity<Square100Game, Square100Document, Square100GameMove, Square100GameState>() {
    @Bean
    protected lateinit var document: Square100Document
    override fun doc() = document

    @AfterViews
    protected override fun init() {
        gameView = Square100GameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = Square100Game(level.layout, this, doc())
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
        Square100HelpActivity_.intent(this).start()
    }
}