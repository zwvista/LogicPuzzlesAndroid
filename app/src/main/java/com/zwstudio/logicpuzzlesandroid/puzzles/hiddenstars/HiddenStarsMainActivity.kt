package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenstars

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class HiddenStarsMainActivity : GameMainActivity<HiddenStarsGame, HiddenStarsDocument, HiddenStarsGameMove, HiddenStarsGameState>() {
    private val document: HiddenStarsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, HiddenStarsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, HiddenStarsGameActivity::class.java))
    }
}

class HiddenStarsOptionsActivity : GameOptionsActivity<HiddenStarsGame, HiddenStarsDocument, HiddenStarsGameMove, HiddenStarsGameState>() {
    private val document: HiddenStarsDocument by inject()
    override val doc get() = document
}

class HiddenStarsHelpActivity : GameHelpActivity<HiddenStarsGame, HiddenStarsDocument, HiddenStarsGameMove, HiddenStarsGameState>() {
    private val document: HiddenStarsDocument by inject()
    override val doc get() = document
}