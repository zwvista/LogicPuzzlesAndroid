package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.data.ParkLakesDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesGame
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class ParkLakesGameActivity : GameGameActivity<ParkLakesGame, ParkLakesDocument, ParkLakesGameMove, ParkLakesGameState>() {
    @Bean
    protected lateinit var document: ParkLakesDocument
    override fun doc() = document

    protected lateinit var gameView2: ParkLakesGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = ParkLakesGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID: String = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.setText(selectedLevelID)
        updateSolutionUI()
        levelInitilizing = true
        game = ParkLakesGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move: ParkLakesGameMove = doc().loadMove(rec)
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
        ParkLakesHelpActivity_.intent(this).start()
    }
}