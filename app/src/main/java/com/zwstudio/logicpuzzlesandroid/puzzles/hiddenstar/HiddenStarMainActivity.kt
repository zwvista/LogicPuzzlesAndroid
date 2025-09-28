package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenstar

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class HiddenStarMainActivity : GameMainActivity<HiddenStarGame, HiddenStarDocument, HiddenStarGameMove, HiddenStarGameState>() {
    private val document: HiddenStarDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, HiddenStarOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, HiddenStarGameActivity::class.java))
    }
}

class HiddenStarOptionsActivity : GameOptionsActivity<HiddenStarGame, HiddenStarDocument, HiddenStarGameMove, HiddenStarGameState>() {
    private val document: HiddenStarDocument by inject()
    override val doc get() = document
}

class HiddenStarHelpActivity : GameHelpActivity<HiddenStarGame, HiddenStarDocument, HiddenStarGameMove, HiddenStarGameState>() {
    private val document: HiddenStarDocument by inject()
    override val doc get() = document
}