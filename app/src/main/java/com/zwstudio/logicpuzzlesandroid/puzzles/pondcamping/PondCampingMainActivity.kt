package com.zwstudio.logicpuzzlesandroid.puzzles.pondcamping

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class PondCampingMainActivity : GameMainActivity<PondCampingGame, PondCampingDocument, PondCampingGameMove, PondCampingGameState>() {
    private val document: PondCampingDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, PondCampingOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, PondCampingGameActivity::class.java))
    }
}

class PondCampingOptionsActivity : GameOptionsActivity<PondCampingGame, PondCampingDocument, PondCampingGameMove, PondCampingGameState>() {
    private val document: PondCampingDocument by inject()
    override val doc get() = document
}

class PondCampingHelpActivity : GameHelpActivity<PondCampingGame, PondCampingDocument, PondCampingGameMove, PondCampingGameState>() {
    private val document: PondCampingDocument by inject()
    override val doc get() = document
}