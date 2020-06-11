package com.zwstudio.logicpuzzlesandroid.puzzles.overunder.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.overunder.data.OverUnderDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.overunder.domain.OverUnderGame
import com.zwstudio.logicpuzzlesandroid.puzzles.overunder.domain.OverUnderGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.overunder.domain.OverUnderGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class OverUnderGameActivity : GameGameActivity<OverUnderGame, OverUnderDocument, OverUnderGameMove, OverUnderGameState>() {
    @Bean
    protected lateinit var document: OverUnderDocument
    override fun doc() = document

    protected lateinit var gameView2: OverUnderGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = OverUnderGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID: String = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.setText(selectedLevelID)
        updateSolutionUI()
        levelInitilizing = true
        game = OverUnderGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move: OverUnderGameMove = doc().loadMove(rec)
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
        OverUnderHelpActivity_.intent(this).start()
    }
}