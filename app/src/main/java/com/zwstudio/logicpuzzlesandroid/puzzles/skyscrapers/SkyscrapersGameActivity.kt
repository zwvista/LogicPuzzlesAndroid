package com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class SkyscrapersGameActivity : GameGameActivity<SkyscrapersGame, SkyscrapersDocument, SkyscrapersGameMove, SkyscrapersGameState>() {
    @Bean
    protected lateinit var document: SkyscrapersDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = SkyscrapersGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        SkyscrapersGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        SkyscrapersHelpActivity_.intent(this).start()
    }
}