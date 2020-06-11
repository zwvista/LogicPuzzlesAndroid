package com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.androidimport

import android.view.View
import fj.data.List
import org.androidannotations.annotations.Bean

_
@EActivity(R.layout.activity_game_game)
class RobotCrosswordsGameActivity : GameGameActivity<RobotCrosswordsGame?, RobotCrosswordsDocument?, RobotCrosswordsGameMove?, RobotCrosswordsGameState?>() {
    @Bean
    protected var document: RobotCrosswordsDocument? = null
    override fun doc() = document

    protected var gameView: RobotCrosswordsGameView? = null
    protected override fun getGameView() = gameView

    @AfterViews
    protected override fun init() {
        gameView = RobotCrosswordsGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID: String = doc().selectedLevelID
        val level: GameLevel = doc().levels.get(List.iterableList<GameLevel>(doc().levels).toStream().indexOf(F<GameLevel, Boolean> { o: GameLevel -> o.id == selectedLevelID }).orSome(0))
        tvLevel.setText(selectedLevelID)
        updateSolutionUI()
        levelInitilizing = true
        game = RobotCrosswordsGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move: RobotCrosswordsGameMove = doc().loadMove(rec)
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
        RobotCrosswordsHelpActivity_.intent(this).start()
    }
}