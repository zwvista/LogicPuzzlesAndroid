package com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class CastleBaileyGameActivity : GameGameActivity<CastleBaileyGame, CastleBaileyDocument, CastleBaileyGameMove, CastleBaileyGameState>() {
    @Bean
    protected lateinit var document: CastleBaileyDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = CastleBaileyGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        CastleBaileyGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        CastleBaileyHelpActivity_.intent(this).start()
    }
}
