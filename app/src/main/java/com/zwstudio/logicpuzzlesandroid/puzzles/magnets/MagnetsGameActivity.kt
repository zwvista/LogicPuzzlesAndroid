package com.zwstudio.logicpuzzlesandroid.puzzles.magnets

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
open class MagnetsGameActivity : GameGameActivity<MagnetsGame, MagnetsDocument, MagnetsGameMove, MagnetsGameState>() {
    @Bean
    protected lateinit var document: MagnetsDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = MagnetsGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        MagnetsGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        MagnetsHelpActivity_.intent(this).start()
    }
}