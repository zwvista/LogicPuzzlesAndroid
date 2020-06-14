package com.zwstudio.logicpuzzlesandroid.puzzles.kropki

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class KropkiGameActivity : GameGameActivity<KropkiGame, KropkiDocument, KropkiGameMove, KropkiGameState>() {
    @Bean
    protected lateinit var document: KropkiDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = KropkiGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        KropkiGame(level.layout, "1" == level.settings["Bordered"], this, doc)

    @Click
    protected fun btnHelp() {
        KropkiHelpActivity_.intent(this).start()
    }
}