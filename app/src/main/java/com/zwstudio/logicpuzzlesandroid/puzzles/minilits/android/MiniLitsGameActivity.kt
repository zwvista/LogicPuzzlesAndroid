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

    protected lateinit var gameView2: MiniLitsGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = MiniLitsGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID: String = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.setText(selectedLevelID)
        updateSolutionUI()
        levelInitilizing = true
        game = MiniLitsGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move: MiniLitsGameMove = doc().loadMove(rec)
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
        MiniLitsHelpActivity_.intent(this).start()
    }
}