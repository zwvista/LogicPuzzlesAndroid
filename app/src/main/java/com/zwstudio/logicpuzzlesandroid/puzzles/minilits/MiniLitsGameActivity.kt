package com.zwstudio.logicpuzzlesandroid.puzzles.minilits

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class MiniLitsGameActivity : GameGameActivity<MiniLitsGame, MiniLitsDocument, MiniLitsGameMove, MiniLitsGameState>() {
    @Bean
    protected lateinit var document: MiniLitsDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = MiniLitsGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        MiniLitsGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        MiniLitsHelpActivity_.intent(this).start()
    }
}