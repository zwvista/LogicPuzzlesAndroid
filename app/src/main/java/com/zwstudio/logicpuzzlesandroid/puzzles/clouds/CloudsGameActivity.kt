package com.zwstudio.logicpuzzlesandroid.puzzles.clouds

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class CloudsGameActivity : GameGameActivity<CloudsGame, CloudsDocument, CloudsGameMove, CloudsGameState>() {
    @Bean
    protected lateinit var document: CloudsDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = CloudsGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        CloudsGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        CloudsHelpActivity_.intent(this).start()
    }
}