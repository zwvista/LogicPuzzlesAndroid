package com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.android

import android.view.View
import fj.data.List
import org.androidannotations.annotations.Bean

@EActivity(R.layout.activity_game_game)
class NeighboursGameActivity : GameGameActivity<NeighboursGame, NeighboursDocument, NeighboursGameMove, NeighboursGameState>() {
    @Bean
    protected var document: NeighboursDocument = null
    override fun doc() = document

    protected var gameView: NeighboursGameView = null
    protected override fun getGameView() = gameView

    @AfterViews
    protected override fun init() {
        gameView2 = NeighboursGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID: String = doc().selectedLevelID
        val level: GameLevel = doc().levels.get(List.iterableList<GameLevel>(doc().levels).toStream().indexOf(F<GameLevel, Boolean> { o: GameLevel -> o.id == selectedLevelID }).orSome(0))
        tvLevel.setText(selectedLevelID)
        updateSolutionUI()
        levelInitilizing = true
        game = NeighboursGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move: NeighboursGameMove = doc().loadMove(rec)
                game.setObject(move)
            }
            val moveIndex: Int = doc().levelProgress().moveIndex
            if (moveIndex >= 0 && moveIndex < game.moveCount()) while (moveIndex != game.moveIndex()) game.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        NeighboursHelpActivity_.intent(this).start()
    }
}