package com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class BWTapaGameActivity : GameGameActivity<BWTapaGame, BWTapaDocument, BWTapaGameMove, BWTapaGameState>() {
    @Bean
    protected lateinit var document: BWTapaDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = BWTapaGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        BWTapaGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        BWTapaHelpActivity_.intent(this).start()
    }
}