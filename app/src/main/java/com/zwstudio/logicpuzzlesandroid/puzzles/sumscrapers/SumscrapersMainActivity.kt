package com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class SumscrapersMainActivity : GameMainActivity<SumscrapersGame, SumscrapersDocument, SumscrapersGameMove, SumscrapersGameState>() {
    private val document: SumscrapersDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, SumscrapersOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, SumscrapersGameActivity::class.java))
    }
}

class SumscrapersOptionsActivity : GameOptionsActivity<SumscrapersGame, SumscrapersDocument, SumscrapersGameMove, SumscrapersGameState>() {
    private val document: SumscrapersDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class SumscrapersHelpActivity : GameHelpActivity<SumscrapersGame, SumscrapersDocument, SumscrapersGameMove, SumscrapersGameState>() {
    private val document: SumscrapersDocument by inject()
    override val doc get() = document
}