package com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class SlitherLinkGameActivity : GameGameActivity<SlitherLinkGame, SlitherLinkDocument, SlitherLinkGameMove, SlitherLinkGameState>() {
    @Bean
    protected lateinit var document: SlitherLinkDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = SlitherLinkGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        SlitherLinkGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        SlitherLinkHelpActivity_.intent(this).start()
    }
}