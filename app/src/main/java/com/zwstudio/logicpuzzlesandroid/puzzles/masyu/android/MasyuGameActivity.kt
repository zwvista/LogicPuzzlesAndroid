package com.zwstudio.logicpuzzlesandroid.puzzles.masyu.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.data.MasyuDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGame
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class MasyuGameActivity : GameGameActivity<MasyuGame, MasyuDocument, MasyuGameMove, MasyuGameState>() {
    @Bean
    protected lateinit var document: MasyuDocument
    override fun doc() = document

    protected lateinit var gameView2: MasyuGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = MasyuGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = MasyuGame(level.layout, this, doc())
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
        MasyuHelpActivity_.intent(this).start()
    }
}