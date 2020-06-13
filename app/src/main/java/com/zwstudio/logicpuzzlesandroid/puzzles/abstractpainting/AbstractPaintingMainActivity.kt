package com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class AbstractPaintingMainActivity : GameMainActivity<AbstractPaintingGame, AbstractPaintingDocument, AbstractPaintingGameMove, AbstractPaintingGameState>() {
    @Bean
    protected lateinit var document: AbstractPaintingDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        AbstractPaintingOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        AbstractPaintingGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_help)
class AbstractPaintingHelpActivity : GameHelpActivity<AbstractPaintingGame, AbstractPaintingDocument, AbstractPaintingGameMove, AbstractPaintingGameState>() {
    @Bean
    protected lateinit var document: AbstractPaintingDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_options)
class AbstractPaintingOptionsActivity : GameOptionsActivity<AbstractPaintingGame, AbstractPaintingDocument, AbstractPaintingGameMove, AbstractPaintingGameState>() {
    @Bean
    protected lateinit var document: AbstractPaintingDocument
    override val doc get() = document
}
