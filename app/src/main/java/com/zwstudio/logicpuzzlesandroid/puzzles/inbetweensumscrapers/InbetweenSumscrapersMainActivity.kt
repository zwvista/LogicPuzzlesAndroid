package com.zwstudio.logicpuzzlesandroid.puzzles.inbetweensumscrapers

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class InbetweenSumscrapersMainActivity : GameMainActivity<InbetweenSumscrapersGame, InbetweenSumscrapersDocument, InbetweenSumscrapersGameMove, InbetweenSumscrapersGameState>() {
    private val document: InbetweenSumscrapersDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, InbetweenSumscrapersOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, InbetweenSumscrapersGameActivity::class.java))
    }
}

class InbetweenSumscrapersOptionsActivity : GameOptionsActivity<InbetweenSumscrapersGame, InbetweenSumscrapersDocument, InbetweenSumscrapersGameMove, InbetweenSumscrapersGameState>() {
    private val document: InbetweenSumscrapersDocument by inject()
    override val doc get() = document
}

class InbetweenSumscrapersHelpActivity : GameHelpActivity<InbetweenSumscrapersGame, InbetweenSumscrapersDocument, InbetweenSumscrapersGameMove, InbetweenSumscrapersGameState>() {
    private val document: InbetweenSumscrapersDocument by inject()
    override val doc get() = document
}