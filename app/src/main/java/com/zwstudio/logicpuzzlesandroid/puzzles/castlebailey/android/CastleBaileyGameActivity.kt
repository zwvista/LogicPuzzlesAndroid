package com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey.data.CastleBaileyDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey.domain.CastleBaileyGame
import com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey.domain.CastleBaileyGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey.domain.CastleBaileyGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class CastleBaileyGameActivity : GameGameActivity<CastleBaileyGame, CastleBaileyDocument, CastleBaileyGameMove, CastleBaileyGameState>() {
    @Bean
    protected lateinit var document: CastleBaileyDocument
    override fun doc() = document

    protected lateinit var gameView2: CastleBaileyGameView
    override fun getGameView() = gameView2

    @AfterViews
    override fun init() {
        gameView2 = CastleBaileyGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()

        levelInitilizing = true
        game = CastleBaileyGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move = doc().loadMove(rec)
                game.setObject(move)
            }
            val moveIndex = doc().levelProgress().moveIndex
            if (moveIndex in 0 until game.moveCount())
                while (moveIndex != game.moveIndex())
                    game.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        CastleBaileyHelpActivity_.intent(this).start()
    }
}
