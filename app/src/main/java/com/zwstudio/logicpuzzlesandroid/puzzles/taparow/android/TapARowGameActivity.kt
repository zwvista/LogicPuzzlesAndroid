package com.zwstudio.logicpuzzlesandroid.puzzles.taparow.android

import android.view.View
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.data.TapARowDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain.TapARowGame
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain.TapARowGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain.TapARowGameState
import fj.data.List
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
open class TapARowGameActivity : GameGameActivity<TapARowGame?, TapARowDocument?, TapARowGameMove?, TapARowGameState?>() {
    @Bean
    protected var document: TapARowDocument? = null
    override fun doc(): TapARowDocument {
        return document!!
    }

    protected var gameView: TapARowGameView? = null
    override fun getGameView(): View {
        return gameView!!
    }

    @AfterViews
    override fun init() {
        gameView = TapARowGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[List.iterableList(doc().levels).toStream().indexOf { o: GameLevel -> o.id == selectedLevelID }.orSome(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = TapARowGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move = doc().loadMove(rec)
                game!!.setObject(move)
            }
            val moveIndex = doc().levelProgress().moveIndex
            if (moveIndex >= 0 && moveIndex < game!!.moveCount()) while (moveIndex != game!!.moveIndex()) game!!.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        TapARowHelpActivity_.intent(this).start()
    }
}