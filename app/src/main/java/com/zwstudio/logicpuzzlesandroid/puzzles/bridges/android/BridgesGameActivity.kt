package com.zwstudio.logicpuzzlesandroid.puzzles.bridges.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.data.BridgesDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGame
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class BridgesGameActivity : GameGameActivity<BridgesGame, BridgesDocument, BridgesGameMove, BridgesGameState>() {
    @Bean
    protected lateinit var document: BridgesDocument
    override fun doc() = document

    @AfterViews
    override fun init() {
        gameView = BridgesGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = BridgesGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move = doc().loadMove(rec)
                game.switchBridges(move)
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
        BridgesHelpActivity_.intent(this).start()
    }
}