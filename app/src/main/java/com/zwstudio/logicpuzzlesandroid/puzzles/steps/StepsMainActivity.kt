package com.zwstudio.logicpuzzlesandroid.puzzles.steps

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class StepsMainActivity : GameMainActivity<StepsGame, StepsDocument, StepsGameMove, StepsGameState>() {
    private val document: StepsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, StepsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, StepsGameActivity::class.java))
    }
}

class StepsOptionsActivity : GameOptionsActivity<StepsGame, StepsDocument, StepsGameMove, StepsGameState>() {
    private val document: StepsDocument by inject()
    override val doc get() = document
}

class StepsHelpActivity : GameHelpActivity<StepsGame, StepsDocument, StepsGameMove, StepsGameState>() {
    private val document: StepsDocument by inject()
    override val doc get() = document
}