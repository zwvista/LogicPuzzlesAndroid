package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenpath

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class HiddenPathMainActivity : GameMainActivity<HiddenPathGame, HiddenPathDocument, HiddenPathGameMove, HiddenPathGameState>() {
    private val document: HiddenPathDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, HiddenPathOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, HiddenPathGameActivity::class.java))
    }
}

class HiddenPathOptionsActivity : GameOptionsActivity<HiddenPathGame, HiddenPathDocument, HiddenPathGameMove, HiddenPathGameState>() {
    private val document: HiddenPathDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class HiddenPathHelpActivity : GameHelpActivity<HiddenPathGame, HiddenPathDocument, HiddenPathGameMove, HiddenPathGameState>() {
    private val document: HiddenPathDocument by inject()
    override val doc get() = document
}
