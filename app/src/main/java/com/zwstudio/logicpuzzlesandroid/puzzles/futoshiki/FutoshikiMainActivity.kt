package com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class FutoshikiMainActivity : GameMainActivity<FutoshikiGame, FutoshikiDocument, FutoshikiGameMove, FutoshikiGameState>() {
    private val document: FutoshikiDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, FutoshikiOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, FutoshikiGameActivity::class.java))
    }
}

class FutoshikiOptionsActivity : GameOptionsActivity<FutoshikiGame, FutoshikiDocument, FutoshikiGameMove, FutoshikiGameState>() {
    private val document: FutoshikiDocument by inject()
    override val doc get() = document
}

class FutoshikiHelpActivity : GameHelpActivity<FutoshikiGame, FutoshikiDocument, FutoshikiGameMove, FutoshikiGameState>() {
    private val document: FutoshikiDocument by inject()
    override val doc get() = document
}