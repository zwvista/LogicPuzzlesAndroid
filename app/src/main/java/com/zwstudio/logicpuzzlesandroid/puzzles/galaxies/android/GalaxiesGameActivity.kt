package com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.android

import android.view.View
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.data.GalaxiesDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.domain.GalaxiesGame
import com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.domain.GalaxiesGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.domain.GalaxiesGameState
import fj.data.List
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
open class GalaxiesGameActivity : GameGameActivity<GalaxiesGame?, GalaxiesDocument?, GalaxiesGameMove?, GalaxiesGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: GalaxiesDocument? = null
    override fun doc(): GalaxiesDocument {
        return document!!
    }

    protected var gameView: GalaxiesGameView? = null
    override fun getGameView(): View {
        return gameView!!
    }

    @AfterViews
    override fun init() {
        gameView = GalaxiesGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[List.iterableList(doc().levels).toStream().indexOf { o: GameLevel -> o.id == selectedLevelID }.orSome(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = GalaxiesGame(level.layout, this, doc())
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
        GalaxiesHelpActivity_.intent(this).start()
    }
}