package com.zwstudio.logicpuzzlesandroid.puzzles.funnynumbers

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class FunnyNumbersMainActivity : GameMainActivity<FunnyNumbersGame, FunnyNumbersDocument, FunnyNumbersGameMove, FunnyNumbersGameState>() {
    private val document: FunnyNumbersDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, FunnyNumbersOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, FunnyNumbersGameActivity::class.java))
    }
}

class FunnyNumbersOptionsActivity : GameOptionsActivity<FunnyNumbersGame, FunnyNumbersDocument, FunnyNumbersGameMove, FunnyNumbersGameState>() {
    private val document: FunnyNumbersDocument by inject()
    override val doc get() = document
}

class FunnyNumbersHelpActivity : GameHelpActivity<FunnyNumbersGame, FunnyNumbersDocument, FunnyNumbersGameMove, FunnyNumbersGameState>() {
    private val document: FunnyNumbersDocument by inject()
    override val doc get() = document
}