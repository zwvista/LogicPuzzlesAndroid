package com.zwstudio.logicpuzzlesandroid.puzzles.mathrax

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class MathraxMainActivity : GameMainActivity<MathraxGame, MathraxDocument, MathraxGameMove, MathraxGameState>() {
    private val document: MathraxDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, MathraxOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, MathraxGameActivity::class.java))
    }
}

class MathraxOptionsActivity : GameOptionsActivity<MathraxGame, MathraxDocument, MathraxGameMove, MathraxGameState>() {
    private val document: MathraxDocument by inject()
    override val doc get() = document
}

class MathraxHelpActivity : GameHelpActivity<MathraxGame, MathraxDocument, MathraxGameMove, MathraxGameState>() {
    private val document: MathraxDocument by inject()
    override val doc get() = document
}