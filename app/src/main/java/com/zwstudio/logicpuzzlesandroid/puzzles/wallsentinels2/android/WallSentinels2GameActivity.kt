package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels2.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels2.data.WallSentinels2Document
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels2.domain.WallSentinels2Game
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels2.domain.WallSentinels2GameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels2.domain.WallSentinels2GameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class WallSentinels2GameActivity : GameGameActivity<WallSentinels2Game, WallSentinels2Document, WallSentinels2GameMove, WallSentinels2GameState>() {
    @Bean
    protected lateinit var document: WallSentinels2Document
    override fun doc() = document

    protected lateinit var gameView2: WallSentinels2GameView
    override fun getGameView() = gameView2

    @AfterViews
    override fun init() {
        gameView2 = WallSentinels2GameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()

        levelInitilizing = true
        game = WallSentinels2Game(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move = doc().loadMove(rec)
                game.setObject(move)
            }
            val moveIndex = doc().levelProgress().moveIndex
            if (!(moveIndex >= 0 && moveIndex < game.moveCount())) return
            while (moveIndex != game.moveIndex())
                game.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() = WallSentinels2HelpActivity_.intent(this).start()
}
