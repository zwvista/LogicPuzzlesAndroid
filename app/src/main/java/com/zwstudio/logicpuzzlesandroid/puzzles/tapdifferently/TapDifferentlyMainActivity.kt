package com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class TapDifferentlyMainActivity : GameMainActivity<TapDifferentlyGame, TapDifferentlyDocument, TapDifferentlyGameMove, TapDifferentlyGameState>() {
    private val document: TapDifferentlyDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, TapDifferentlyOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, TapDifferentlyGameActivity::class.java))
    }
}

class TapDifferentlyOptionsActivity : GameOptionsActivity<TapDifferentlyGame, TapDifferentlyDocument, TapDifferentlyGameMove, TapDifferentlyGameState>() {
    private val document: TapDifferentlyDocument by inject()
    override val doc get() = document
}

class TapDifferentlyHelpActivity : GameHelpActivity<TapDifferentlyGame, TapDifferentlyDocument, TapDifferentlyGameMove, TapDifferentlyGameState>() {
    private val document: TapDifferentlyDocument by inject()
    override val doc get() = document
}