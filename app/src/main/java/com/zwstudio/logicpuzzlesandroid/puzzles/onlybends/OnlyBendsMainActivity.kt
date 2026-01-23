package com.zwstudio.logicpuzzlesandroid.puzzles.onlybends

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class OnlyBendsMainActivity : GameMainActivity<OnlyBendsGame, OnlyBendsDocument, OnlyBendsGameMove, OnlyBendsGameState>() {
    private val document: OnlyBendsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, OnlyBendsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, OnlyBendsGameActivity::class.java))
    }
}

class OnlyBendsOptionsActivity : GameOptionsActivity<OnlyBendsGame, OnlyBendsDocument, OnlyBendsGameMove, OnlyBendsGameState>() {
    private val document: OnlyBendsDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class OnlyBendsHelpActivity : GameHelpActivity<OnlyBendsGame, OnlyBendsDocument, OnlyBendsGameMove, OnlyBendsGameState>() {
    private val document: OnlyBendsDocument by inject()
    override val doc get() = document
}