package com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.data.AbstractPaintingDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.domain.AbstractPaintingGame
import com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.domain.AbstractPaintingGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.domain.AbstractPaintingGameState

import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
open class AbstractPaintingMainActivity : GameMainActivity<AbstractPaintingGame, AbstractPaintingDocument, AbstractPaintingGameMove, AbstractPaintingGameState>() {
    @Bean
    protected var document: AbstractPaintingDocument? = null

    override fun doc(): AbstractPaintingDocument? {
        return document
    }

    @Click
    internal fun btnOptions() {
        AbstractPaintingOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc()!!.resumeGame()
        AbstractPaintingGameActivity_.intent(this).start()
    }
}
