package com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class AbstractPaintingMainActivity : GameMainActivity<AbstractPaintingGame, AbstractPaintingDocument, AbstractPaintingGameMove, AbstractPaintingGameState>() {
    private val document: AbstractPaintingDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, AbstractPaintingOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, AbstractPaintingGameActivity::class.java))
    }
}

class AbstractPaintingHelpActivity : GameHelpActivity<AbstractPaintingGame, AbstractPaintingDocument, AbstractPaintingGameMove, AbstractPaintingGameState>() {
    private val document: AbstractPaintingDocument by inject()
    override val doc get() = document
}

class AbstractPaintingOptionsActivity : GameOptionsActivity<AbstractPaintingGame, AbstractPaintingDocument, AbstractPaintingGameMove, AbstractPaintingGameState>() {
    private val document: AbstractPaintingDocument by inject()
    override val doc get() = document
}
