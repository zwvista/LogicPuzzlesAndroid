package com.zwstudio.logicpuzzlesandroid.puzzles.masyu

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class MasyuGameActivity : GameGameActivity<MasyuGame, MasyuDocument, MasyuGameMove, MasyuGameState>() {
    @Bean
    protected lateinit var document: MasyuDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = MasyuGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        MasyuGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        MasyuHelpActivity_.intent(this).start()
    }
}