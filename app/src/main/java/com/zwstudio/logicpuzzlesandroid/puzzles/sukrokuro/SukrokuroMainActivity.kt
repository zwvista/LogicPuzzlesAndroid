package com.zwstudio.logicpuzzlesandroid.puzzles.sukrokuro

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class SukrokuroMainActivity : GameMainActivity<SukrokuroGame, SukrokuroDocument, SukrokuroGameMove, SukrokuroGameState>() {
    private val document: SukrokuroDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, SukrokuroOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, SukrokuroGameActivity::class.java))
    }
}

class SukrokuroOptionsActivity : GameOptionsActivity<SukrokuroGame, SukrokuroDocument, SukrokuroGameMove, SukrokuroGameState>() {
    private val document: SukrokuroDocument by inject()
    override val doc get() = document
}

class SukrokuroHelpActivity : GameHelpActivity<SukrokuroGame, SukrokuroDocument, SukrokuroGameMove, SukrokuroGameState>() {
    private val document: SukrokuroDocument by inject()
    override val doc get() = document
}