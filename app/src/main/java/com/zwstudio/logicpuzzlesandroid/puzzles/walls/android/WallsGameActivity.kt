package com.zwstudio.logicpuzzlesandroid.puzzles.walls.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.data.WallsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain.WallsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain.WallsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain.WallsGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class WallsGameActivity : GameGameActivity<WallsGame, WallsDocument, WallsGameMove, WallsGameState>() {
    @Bean
    protected lateinit var document: WallsDocument
    override fun doc() = document

    protected lateinit var gameView2: WallsGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = WallsGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = WallsGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move = doc().loadMove(rec)
                game.setObject(move)
            }
            val moveIndex = doc().levelProgress().moveIndex
            if (moveIndex >= 0 && moveIndex < game.moveCount())
                while (moveIndex != game.moveIndex())
                    game.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        WallsHelpActivity_.intent(this).start()
    }
}