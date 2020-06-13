package com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.data.SentinelsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.domain.SentinelsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.domain.SentinelsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.domain.SentinelsGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class SentinelsGameActivity : GameGameActivity<SentinelsGame, SentinelsDocument, SentinelsGameMove, SentinelsGameState>() {
    @Bean
    protected lateinit var document: SentinelsDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = SentinelsGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID = doc.selectedLevelID
        val level = doc.levels[doc.levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = SentinelsGame(level.layout, this, doc)
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
        SentinelsHelpActivity_.intent(this).start()
    }
}