package com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.data.TennerGridDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain.TennerGridGame
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain.TennerGridGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain.TennerGridGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class TennerGridGameActivity : GameGameActivity<TennerGridGame, TennerGridDocument, TennerGridGameMove, TennerGridGameState>() {
    @Bean
    protected lateinit var document: TennerGridDocument
    override fun doc() = document

    protected lateinit var gameView2: TennerGridGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = TennerGridGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = TennerGridGame(level.layout, this, doc())
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
        TennerGridHelpActivity_.intent(this).start()
    }
}