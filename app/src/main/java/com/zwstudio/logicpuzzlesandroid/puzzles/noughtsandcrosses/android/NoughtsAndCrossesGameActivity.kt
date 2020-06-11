package com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses.androidimport

import android.view.View
import fj.data.List
import org.androidannotations.annotations.Bean

@EActivity(R.layout.activity_game_game)
class NoughtsAndCrossesGameActivity : GameGameActivity<NoughtsAndCrossesGame, NoughtsAndCrossesDocument, NoughtsAndCrossesGameMove, NoughtsAndCrossesGameState>() {
    @Bean
    protected var document: NoughtsAndCrossesDocument = null
    override fun doc() = document

    protected var gameView: NoughtsAndCrossesGameView = null
    protected override fun getGameView() = gameView

    @AfterViews
    protected override fun init() {
        gameView2 = NoughtsAndCrossesGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID: String = doc().selectedLevelID
        val level: GameLevel = doc().levels.get(List.iterableList<GameLevel>(doc().levels).toStream().indexOf(F<GameLevel, Boolean> { o: GameLevel -> o.id == selectedLevelID }).orSome(0))
        tvLevel.setText(selectedLevelID)
        updateSolutionUI()
        levelInitilizing = true
        game = NoughtsAndCrossesGame(level.layout, level.settings.get("num").get(0), this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move: NoughtsAndCrossesGameMove = doc().loadMove(rec)
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
        NoughtsAndCrossesHelpActivity_.intent(this).start()
    }
}