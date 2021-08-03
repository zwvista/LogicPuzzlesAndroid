package com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class AbstractPaintingGameActivity : GameGameActivity<AbstractPaintingGame, AbstractPaintingDocument, AbstractPaintingGameMove, AbstractPaintingGameState>() {
    private val document: AbstractPaintingDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = AbstractPaintingGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, AbstractPaintingHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        AbstractPaintingGame(level.layout, this, doc)
}
