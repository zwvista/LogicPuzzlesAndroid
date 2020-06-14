package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class BootyIslandGameActivity : GameGameActivity<BootyIslandGame, BootyIslandDocument, BootyIslandGameMove, BootyIslandGameState>() {
    @Bean
    protected lateinit var document: BootyIslandDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = BootyIslandGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        BootyIslandGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        BootyIslandHelpActivity_.intent(this).start()
    }
}
