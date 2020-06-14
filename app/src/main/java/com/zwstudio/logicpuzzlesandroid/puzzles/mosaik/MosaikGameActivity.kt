package com.zwstudio.logicpuzzlesandroid.puzzles.mosaik

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class MosaikGameActivity : GameGameActivity<MosaikGame, MosaikDocument, MosaikGameMove, MosaikGameState>() {
    @Bean
    protected lateinit var document: MosaikDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = MosaikGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        MosaikGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        MosaikHelpActivity_.intent(this).start()
    }
}