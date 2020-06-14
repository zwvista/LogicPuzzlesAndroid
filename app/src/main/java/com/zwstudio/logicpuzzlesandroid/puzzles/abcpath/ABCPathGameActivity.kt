package com.zwstudio.logicpuzzlesandroid.puzzles.abcpath

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class ABCPathGameActivity : GameGameActivity<ABCPathGame, ABCPathDocument, ABCPathGameMove, ABCPathGameState>() {
    @Bean
    protected lateinit var document: ABCPathDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = ABCPathGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        ABCPathGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        ABCPathHelpActivity_.intent(this).start()
    }
}
