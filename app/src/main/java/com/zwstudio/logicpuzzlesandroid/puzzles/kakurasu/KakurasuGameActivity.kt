package com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class KakurasuGameActivity : GameGameActivity<KakurasuGame, KakurasuDocument, KakurasuGameMove, KakurasuGameState>() {
    @Bean
    protected lateinit var document: KakurasuDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = KakurasuGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        KakurasuGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        KakurasuHelpActivity_.intent(this).start()
    }
}