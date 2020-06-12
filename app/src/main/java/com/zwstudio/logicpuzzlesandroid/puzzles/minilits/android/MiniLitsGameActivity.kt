package com.zwstudio.logicpuzzlesandroid.puzzles.minilits.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.data.MiniLitsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class MiniLitsGameActivity : GameGameActivity<MiniLitsGame, MiniLitsDocument, MiniLitsGameMove, MiniLitsGameState>() {
    @Bean
    protected lateinit var document: MiniLitsDocument
    override fun doc() = document

    @AfterViews
    protected override fun init() {
        gameView = MiniLitsGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = MiniLitsGame(level.layout, this, doc())
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
        MiniLitsHelpActivity_.intent(this).start()
    }
}