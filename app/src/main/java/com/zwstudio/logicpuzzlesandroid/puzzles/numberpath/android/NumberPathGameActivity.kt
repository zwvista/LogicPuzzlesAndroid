package com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.data.NumberPathDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.domain.NumberPathGame
import com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.domain.NumberPathGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.domain.NumberPathGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class NumberPathGameActivity : GameGameActivity<NumberPathGame, NumberPathDocument, NumberPathGameMove, NumberPathGameState>() {
    @Bean
    protected lateinit var document: NumberPathDocument
    override fun doc() = document

    protected lateinit var gameView2: NumberPathGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = NumberPathGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID: String = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = NumberPathGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move: NumberPathGameMove = doc().loadMove(rec)
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
        NumberPathHelpActivity_.intent(this).start()
    }
}