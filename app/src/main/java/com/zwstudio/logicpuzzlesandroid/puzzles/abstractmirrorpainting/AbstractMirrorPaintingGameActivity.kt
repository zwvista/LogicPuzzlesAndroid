package com.zwstudio.logicpuzzlesandroid.puzzles.abstractmirrorpainting

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class AbstractMirrorPaintingGameActivity : GameGameActivity<AbstractMirrorPaintingGame, AbstractMirrorPaintingDocument, AbstractMirrorPaintingGameMove, AbstractMirrorPaintingGameState>() {
    private val document: AbstractMirrorPaintingDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = AbstractMirrorPaintingGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, AbstractMirrorPaintingHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        AbstractMirrorPaintingGame(level.layout, this, doc)
}