package com.zwstudio.logicpuzzlesandroid.puzzles.kakuro

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class KakuroGameActivity : GameGameActivity<KakuroGame, KakuroDocument, KakuroGameMove, KakuroGameState>() {
    @Bean
    protected lateinit var document: KakuroDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = KakuroGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        KakuroGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        KakuroHelpActivity_.intent(this).start()
    }
}