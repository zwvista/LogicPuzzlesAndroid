package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.data.NurikabeDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGame
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class NurikabeGameActivity : GameGameActivity<NurikabeGame, NurikabeDocument, NurikabeGameMove, NurikabeGameState>() {
    @Bean
    protected lateinit var document: NurikabeDocument
    override fun doc() = document

    protected lateinit var gameView2: NurikabeGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = NurikabeGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = NurikabeGame(level.layout, this, doc())
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
        NurikabeHelpActivity_.intent(this).start()
    }
}