package com.zwstudio.logicpuzzlesandroid.puzzles.pata.android

import android.view.View
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.data.PataDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain.PataGame
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain.PataGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain.PataGameState
import fj.data.List
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class PataGameActivity : GameGameActivity<PataGame, PataDocument, PataGameMove, PataGameState>() {
    @Bean
    protected lateinit var document: PataDocument
    override fun doc() = document

    protected lateinit var gameView2: PataGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = PataGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = PataGame(level.layout, this, doc())
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
        PataHelpActivity_.intent(this).start()
    }
}