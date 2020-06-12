package com.zwstudio.logicpuzzlesandroid.puzzles.mineships.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.data.MineShipsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class MineShipsGameActivity : GameGameActivity<MineShipsGame, MineShipsDocument, MineShipsGameMove, MineShipsGameState>() {
    @Bean
    protected lateinit var document: MineShipsDocument
    override fun doc() = document

    @AfterViews
    protected override fun init() {
        gameView = MineShipsGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = MineShipsGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move = doc().loadMove(rec)
                game.setObject(move)
            }
            val moveIndex = doc().levelProgress().moveIndex
            if (moveIndex in 0 until game.moveCount)
                while (moveIndex != game.moveIndex)
                    game.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        MineShipsHelpActivity_.intent(this).start()
    }
}