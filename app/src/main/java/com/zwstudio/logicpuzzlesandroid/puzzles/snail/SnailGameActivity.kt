package com.zwstudio.logicpuzzlesandroid.puzzles.snail

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class SnailGameActivity : GameGameActivity<SnailGame, SnailDocument, SnailGameMove, SnailGameState>() {
    @Bean
    protected lateinit var document: SnailDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = SnailGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        SnailGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        SnailHelpActivity_.intent(this).start()
    }
}