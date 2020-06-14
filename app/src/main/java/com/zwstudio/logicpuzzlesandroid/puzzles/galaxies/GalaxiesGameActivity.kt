package com.zwstudio.logicpuzzlesandroid.puzzles.galaxies

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class GalaxiesGameActivity : GameGameActivity<GalaxiesGame, GalaxiesDocument, GalaxiesGameMove, GalaxiesGameState>() {
    @Bean
    protected lateinit var document: GalaxiesDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = GalaxiesGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        GalaxiesGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        GalaxiesHelpActivity_.intent(this).start()
    }
}