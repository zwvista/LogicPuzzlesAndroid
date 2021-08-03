package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class FourMeNotMainActivity : GameMainActivity<FourMeNotGame, FourMeNotDocument, FourMeNotGameMove, FourMeNotGameState>() {
    private val document: FourMeNotDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, FourMeNotOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, FourMeNotGameActivity::class.java))
    }
}

class FourMeNotOptionsActivity : GameOptionsActivity<FourMeNotGame, FourMeNotDocument, FourMeNotGameMove, FourMeNotGameState>() {
    private val document: FourMeNotDocument by inject()
    override val doc get() = document
}

class FourMeNotHelpActivity : GameHelpActivity<FourMeNotGame, FourMeNotDocument, FourMeNotGameMove, FourMeNotGameState>() {
    private val document: FourMeNotDocument by inject()
    override val doc get() = document
}