package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.android

import android.view.View
import fj.data.List
import org.androidannotations.annotations.Bean

@EActivity(R.layout.activity_game_game)
class PairakabeGameActivity : GameGameActivity<PairakabeGame, PairakabeDocument, PairakabeGameMove, PairakabeGameState>() {
    @Bean
    protected var document: PairakabeDocument = null
    override fun doc() = document

    protected var gameView: PairakabeGameView = null
    protected override fun getGameView() = gameView

    @AfterViews
    protected override fun init() {
        gameView2 = PairakabeGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID: String = doc().selectedLevelID
        val level: GameLevel = doc().levels.get(List.iterableList<GameLevel>(doc().levels).toStream().indexOf(F<GameLevel, Boolean> { o: GameLevel -> o.id == selectedLevelID }).orSome(0))
        tvLevel.setText(selectedLevelID)
        updateSolutionUI()
        levelInitilizing = true
        game = PairakabeGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move: PairakabeGameMove = doc().loadMove(rec)
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
        PairakabeHelpActivity_.intent(this).start()
    }
}