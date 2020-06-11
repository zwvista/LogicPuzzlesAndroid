package com.zwstudio.logicpuzzlesandroid.puzzles.theoddbrick.androidimport

import android.view.View
import fj.data.List
import org.androidannotations.annotations.Bean

_
@EActivity(R.layout.activity_game_game)
class TheOddBrickGameActivity : GameGameActivity<TheOddBrickGame?, TheOddBrickDocument?, TheOddBrickGameMove?, TheOddBrickGameState?>() {
    @Bean
    protected var document: TheOddBrickDocument? = null
    override fun doc() = document

    protected var gameView: TheOddBrickGameView? = null
    protected override fun getGameView() = gameView

    @AfterViews
    protected override fun init() {
        gameView = TheOddBrickGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID: String = doc().selectedLevelID
        val level: GameLevel = doc().levels.get(List.iterableList<GameLevel>(doc().levels).toStream().indexOf(F<GameLevel, Boolean> { o: GameLevel -> o.id == selectedLevelID }).orSome(0))
        tvLevel.setText(selectedLevelID)
        updateSolutionUI()
        levelInitilizing = true
        game = TheOddBrickGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move: TheOddBrickGameMove = doc().loadMove(rec)
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
        TheOddBrickHelpActivity_.intent(this).start()
    }
}