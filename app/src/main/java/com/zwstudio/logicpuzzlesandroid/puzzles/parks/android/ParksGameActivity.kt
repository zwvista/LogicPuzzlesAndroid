package com.zwstudio.logicpuzzlesandroid.puzzles.parks.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.data.ParksDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain.ParksGame
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain.ParksGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain.ParksGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class ParksGameActivity : GameGameActivity<ParksGame, ParksDocument, ParksGameMove, ParksGameState>() {
    @Bean
    protected lateinit var document: ParksDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = ParksGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID = doc.selectedLevelID
        val level = doc.levels[doc.levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        val treesInEachArea = (level.settings["TreesInEachArea"] ?: "1").toInt()
        game = ParksGame(level.layout, treesInEachArea, this, doc)
        try {
            // restore game state
            for (rec in doc.moveProgress()) {
                val move = doc.loadMove(rec)
                game.setObject(move)
            }
            val moveIndex = doc.levelProgress().moveIndex
            if (moveIndex in 0 until game.moveCount)
                while (moveIndex != game.moveIndex)
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