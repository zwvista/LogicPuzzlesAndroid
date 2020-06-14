package com.zwstudio.logicpuzzlesandroid.puzzles.theoddbrick

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class TheOddBrickGameActivity : GameGameActivity<TheOddBrickGame, TheOddBrickDocument, TheOddBrickGameMove, TheOddBrickGameState>() {
    @Bean
    protected lateinit var document: TheOddBrickDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = TheOddBrickGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        TheOddBrickGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        TheOddBrickHelpActivity_.intent(this).start()
    }
}