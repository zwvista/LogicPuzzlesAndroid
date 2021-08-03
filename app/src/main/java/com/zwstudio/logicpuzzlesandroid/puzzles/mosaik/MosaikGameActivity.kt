package com.zwstudio.logicpuzzlesandroid.puzzles.mosaik

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class MosaikGameActivity : GameGameActivity<MosaikGame, MosaikDocument, MosaikGameMove, MosaikGameState>() {
    private val document: MosaikDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = MosaikGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, MosaikHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        MosaikGame(level.layout, this, doc)
}