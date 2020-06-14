package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class LighthousesGameActivity : GameGameActivity<LighthousesGame, LighthousesDocument, LighthousesGameMove, LighthousesGameState>() {
    @Bean
    protected lateinit var document: LighthousesDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = LighthousesGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        LighthousesGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        LighthousesHelpActivity_.intent(this).start()
    }
}