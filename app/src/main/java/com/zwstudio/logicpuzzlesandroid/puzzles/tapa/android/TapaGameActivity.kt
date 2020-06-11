package com.zwstudio.logicpuzzlesandroid.puzzles.tapa.androidimport

import android.view.View
import fj.data.List
import org.androidannotations.annotations.Bean

_
@EActivity(R.layout.activity_game_game)
class TapaGameActivity : GameGameActivity<TapaGame?, TapaDocument?, TapaGameMove?, TapaGameState?>() {
    @Bean
    protected var document: TapaDocument? = null
    override fun doc() = document

    protected var gameView: TapaGameView? = null
    protected override fun getGameView() = gameView

    @AfterViews
    protected override fun init() {
        gameView = TapaGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID: String = doc().selectedLevelID
        val level: GameLevel = doc().levels.get(List.iterableList<GameLevel>(doc().levels).toStream().indexOf(F<GameLevel, Boolean> { o: GameLevel -> o.id == selectedLevelID }).orSome(0))
        tvLevel.setText(selectedLevelID)
        updateSolutionUI()
        levelInitilizing = true
        game = TapaGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move: TapaGameMove = doc().loadMove(rec)
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
        TapaHelpActivity_.intent(this).start()
    }
}