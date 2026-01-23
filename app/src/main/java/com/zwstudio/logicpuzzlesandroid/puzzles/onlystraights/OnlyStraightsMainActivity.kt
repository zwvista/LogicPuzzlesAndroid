package com.zwstudio.logicpuzzlesandroid.puzzles.onlystraights

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class OnlyStraightsMainActivity : GameMainActivity<OnlyStraightsGame, OnlyStraightsDocument, OnlyStraightsGameMove, OnlyStraightsGameState>() {
    private val document: OnlyStraightsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, OnlyStraightsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, OnlyStraightsGameActivity::class.java))
    }
}

class OnlyStraightsOptionsActivity : GameOptionsActivity<OnlyStraightsGame, OnlyStraightsDocument, OnlyStraightsGameMove, OnlyStraightsGameState>() {
    private val document: OnlyStraightsDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class OnlyStraightsHelpActivity : GameHelpActivity<OnlyStraightsGame, OnlyStraightsDocument, OnlyStraightsGameMove, OnlyStraightsGameState>() {
    private val document: OnlyStraightsDocument by inject()
    override val doc get() = document
}