package com.zwstudio.logicpuzzlesandroid.puzzles.tatami.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.data.TatamiDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.domain.TatamiGame
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.domain.TatamiGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.domain.TatamiGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class TatamiGameActivity : GameGameActivity<TatamiGame, TatamiDocument, TatamiGameMove, TatamiGameState>() {
    @Bean
    protected lateinit var document: TatamiDocument
    override fun doc() = document

    @AfterViews
    protected override fun init() {
        gameView = TatamiGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = TatamiGame(level.layout, this, doc())
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
        TatamiHelpActivity_.intent(this).start()
    }
}