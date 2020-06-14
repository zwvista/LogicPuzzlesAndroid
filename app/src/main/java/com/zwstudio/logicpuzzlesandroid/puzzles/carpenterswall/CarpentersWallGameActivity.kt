package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class CarpentersWallGameActivity : GameGameActivity<CarpentersWallGame, CarpentersWallDocument, CarpentersWallGameMove, CarpentersWallGameState>() {
    @Bean
    protected lateinit var document: CarpentersWallDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = CarpentersWallGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        CarpentersWallGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        CarpentersWallHelpActivity_.intent(this).start()
    }
}