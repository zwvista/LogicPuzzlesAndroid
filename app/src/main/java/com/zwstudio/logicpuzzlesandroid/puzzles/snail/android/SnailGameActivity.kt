package com.zwstudio.logicpuzzlesandroid.puzzles.snail.android

import android.view.View
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.data.SnailDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.domain.SnailGame
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.domain.SnailGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.domain.SnailGameState
import fj.data.List
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class SnailGameActivity : GameGameActivity<SnailGame, SnailDocument, SnailGameMove, SnailGameState>() {
    @Bean
    protected lateinit var document: SnailDocument
    override fun doc() = document

    protected lateinit var gameView2: SnailGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = SnailGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = SnailGame(level.layout, this, doc())
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
        SnailHelpActivity_.intent(this).start()
    }
}