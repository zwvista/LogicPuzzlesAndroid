package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class ParkLakesGameActivity : GameGameActivity<ParkLakesGame, ParkLakesDocument, ParkLakesGameMove, ParkLakesGameState>() {
    @Bean
    protected lateinit var document: ParkLakesDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = ParkLakesGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        ParkLakesGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        ParkLakesHelpActivity_.intent(this).start()
    }
}