package com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.android

import android.view.View

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.data.AbstractPaintingDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.domain.AbstractPaintingGame
import com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.domain.AbstractPaintingGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.domain.AbstractPaintingGameState

import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

import fj.data.List.iterableList

@EActivity(R.layout.activity_game_game)
open class AbstractPaintingGameActivity : GameGameActivity<AbstractPaintingGame, AbstractPaintingDocument, AbstractPaintingGameMove, AbstractPaintingGameState>() {
    @Bean
    protected lateinit var document: AbstractPaintingDocument
    override fun doc() = document

    protected lateinit var gameView2: AbstractPaintingGameView
    override fun getGameView() = gameView2

    @AfterViews
    override fun init() {
        gameView2 = AbstractPaintingGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()

        levelInitilizing = true
        game = AbstractPaintingGame(level.layout, this, doc())
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
        AbstractPaintingHelpActivity_.intent(this).start()
    }
}
