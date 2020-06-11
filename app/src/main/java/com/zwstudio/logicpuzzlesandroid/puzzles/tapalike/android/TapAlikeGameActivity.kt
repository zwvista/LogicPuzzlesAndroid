package com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.android

import android.view.View
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.data.TapAlikeDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.domain.TapAlikeGame
import com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.domain.TapAlikeGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.domain.TapAlikeGameState
import fj.data.List
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class TapAlikeGameActivity : GameGameActivity<TapAlikeGame, TapAlikeDocument, TapAlikeGameMove, TapAlikeGameState>() {
    @Bean
    protected lateinit var document: TapAlikeDocument
    override fun doc() = document

    protected lateinit var gameView2: TapAlikeGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = TapAlikeGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID: String = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.setText(selectedLevelID)
        updateSolutionUI()
        levelInitilizing = true
        game = TapAlikeGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move: TapAlikeGameMove = doc().loadMove(rec)
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
        TapAlikeHelpActivity_.intent(this).start()
    }
}