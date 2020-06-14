package com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class BoxItAgainGameActivity : GameGameActivity<BoxItAgainGame, BoxItAgainDocument, BoxItAgainGameMove, BoxItAgainGameState>() {
    @Bean
    protected lateinit var document: BoxItAgainDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = BoxItAgainGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        BoxItAgainGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        BoxItAgainHelpActivity_.intent(this).start()
    }
}