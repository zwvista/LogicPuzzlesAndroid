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
    override fun doc() = document

    protected lateinit var gameView2: SentinelsGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = SentinelsGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID: String = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.setText(selectedLevelID)
        updateSolutionUI()
        levelInitilizing = true
        game = SentinelsGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move: SentinelsGameMove = doc().loadMove(rec)
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
        SentinelsHelpActivity_.intent(this).start()
    }
}