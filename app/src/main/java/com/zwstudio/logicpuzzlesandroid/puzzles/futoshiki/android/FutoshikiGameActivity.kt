package com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.data.FutoshikiDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain.FutoshikiGame
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain.FutoshikiGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain.FutoshikiGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class FutoshikiGameActivity : GameGameActivity<FutoshikiGame, FutoshikiDocument, FutoshikiGameMove, FutoshikiGameState>() {
    @Bean
    protected lateinit var document: FutoshikiDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = FutoshikiGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc.selectedLevelID
        val level = doc.levels[doc.levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = FutoshikiGame(level.layout, this, doc)
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
        FutoshikiHelpActivity_.intent(this).start()
    }
}