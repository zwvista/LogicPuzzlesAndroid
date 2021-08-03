package com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class SkyscrapersMainActivity : GameMainActivity<SkyscrapersGame, SkyscrapersDocument, SkyscrapersGameMove, SkyscrapersGameState>() {
    private val document: SkyscrapersDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, SkyscrapersOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, SkyscrapersGameActivity::class.java))
    }
}

class SkyscrapersOptionsActivity : GameOptionsActivity<SkyscrapersGame, SkyscrapersDocument, SkyscrapersGameMove, SkyscrapersGameState>() {
    private val document: SkyscrapersDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class SkyscrapersHelpActivity : GameHelpActivity<SkyscrapersGame, SkyscrapersDocument, SkyscrapersGameMove, SkyscrapersGameState>() {
    private val document: SkyscrapersDocument by inject()
    override val doc get() = document
}