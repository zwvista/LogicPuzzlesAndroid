package com.zwstudio.logicpuzzlesandroid.puzzles.parks.androidimport

import android.view.View
import fj.data.List
import org.androidannotations.annotations.Bean

@EActivity(R.layout.activity_game_game)
class ParksGameActivity : GameGameActivity<ParksGame, ParksDocument, ParksGameMove, ParksGameState>() {
    @Bean
    protected var document: ParksDocument = null
    override fun doc() = document

    protected var gameView: ParksGameView = null
    protected override fun getGameView() = gameView

    @AfterViews
    protected override fun init() {
        gameView2 = ParksGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID: String = doc().selectedLevelID
        val level: GameLevel = doc().levels.get(List.iterableList<GameLevel>(doc().levels).toStream().indexOf(F<GameLevel, Boolean> { o: GameLevel -> o.id == selectedLevelID }).orSome(0))
        tvLevel.setText(selectedLevelID)
        updateSolutionUI()
        levelInitilizing = true
        val treesInEachArea: Int = ObjectUtils.defaultIfNull(level.settings.get("TreesInEachArea"), "1").toInt()
        game = ParksGame(level.layout, treesInEachArea, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move: ParksGameMove = doc().loadMove(rec)
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
        ParksHelpActivity_.intent(this).start()
    }
}