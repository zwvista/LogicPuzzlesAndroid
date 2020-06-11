package com.zwstudio.logicpuzzlesandroid.puzzles.orchards.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.data.OrchardsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain.OrchardsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain.OrchardsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain.OrchardsGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class OrchardsGameActivity : GameGameActivity<OrchardsGame, OrchardsDocument, OrchardsGameMove, OrchardsGameState>() {
    @Bean
    protected lateinit var document: OrchardsDocument
    override fun doc() = document

    protected lateinit var gameView2: OrchardsGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = OrchardsGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID: String = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.setText(selectedLevelID)
        updateSolutionUI()
        levelInitilizing = true
        game = OrchardsGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move: OrchardsGameMove = doc().loadMove(rec)
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
        OrchardsHelpActivity_.intent(this).start()
    }
}