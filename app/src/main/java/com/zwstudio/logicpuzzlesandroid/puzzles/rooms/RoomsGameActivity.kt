package com.zwstudio.logicpuzzlesandroid.puzzles.rooms

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class RoomsGameActivity : GameGameActivity<RoomsGame, RoomsDocument, RoomsGameMove, RoomsGameState>() {
    @Bean
    protected lateinit var document: RoomsDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = RoomsGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        RoomsGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        RoomsHelpActivity_.intent(this).start()
    }
}