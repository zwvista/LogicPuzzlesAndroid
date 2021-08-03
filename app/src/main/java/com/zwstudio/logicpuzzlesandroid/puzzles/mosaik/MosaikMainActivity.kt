package com.zwstudio.logicpuzzlesandroid.puzzles.mosaik

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class MosaikMainActivity : GameMainActivity<MosaikGame, MosaikDocument, MosaikGameMove, MosaikGameState>() {
    private val document: MosaikDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, MosaikOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, MosaikGameActivity::class.java))
    }
}

class MosaikOptionsActivity : GameOptionsActivity<MosaikGame, MosaikDocument, MosaikGameMove, MosaikGameState>() {
    private val document: MosaikDocument by inject()
    override val doc get() = document
}

class MosaikHelpActivity : GameHelpActivity<MosaikGame, MosaikDocument, MosaikGameMove, MosaikGameState>() {
    private val document: MosaikDocument by inject()
    override val doc get() = document
}