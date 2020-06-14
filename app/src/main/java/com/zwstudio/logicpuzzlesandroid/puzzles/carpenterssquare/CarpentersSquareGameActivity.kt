package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class CarpentersSquareGameActivity : GameGameActivity<CarpentersSquareGame, CarpentersSquareDocument, CarpentersSquareGameMove, CarpentersSquareGameState>() {
    @Bean
    protected lateinit var document: CarpentersSquareDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = CarpentersSquareGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        CarpentersSquareGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        CarpentersSquareHelpActivity_.intent(this).start()
    }
}