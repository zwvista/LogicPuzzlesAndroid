package com.zwstudio.logicpuzzlesandroid.puzzles.parks.android

import android.view.View
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.data.ParksDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain.ParksGame
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain.ParksGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain.ParksGameState
import fj.data.List
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class ParksGameActivity : GameGameActivity<ParksGame, ParksDocument, ParksGameMove, ParksGameState>() {
    @Bean
    protected lateinit var document: ParksDocument
    override fun doc() = document

    protected lateinit var gameView2: ParksGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = ParksGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID: String = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
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
            if (moveIndex >= 0 && moveIndex < game.moveCount())
                while (moveIndex != game.moveIndex())
                    game.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        ParksHelpActivity_.intent(this).start()
    }
}