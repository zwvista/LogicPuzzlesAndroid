package com.zwstudio.logicpuzzlesandroid.puzzles.northpolefishing

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class NorthPoleFishingGameActivity : GameGameActivity<NorthPoleFishingGame, NorthPoleFishingDocument, NorthPoleFishingGameMove, NorthPoleFishingGameState>() {
    @Bean
    protected lateinit var document: NorthPoleFishingDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = NorthPoleFishingGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        NorthPoleFishingGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        NorthPoleFishingHelpActivity_.intent(this).start()
    }
}
