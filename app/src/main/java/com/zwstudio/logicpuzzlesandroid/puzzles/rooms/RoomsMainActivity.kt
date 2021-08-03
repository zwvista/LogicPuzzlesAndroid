package com.zwstudio.logicpuzzlesandroid.puzzles.rooms

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class RoomsMainActivity : GameMainActivity<RoomsGame, RoomsDocument, RoomsGameMove, RoomsGameState>() {
    private val document: RoomsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, RoomsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, RoomsGameActivity::class.java))
    }
}

class RoomsOptionsActivity : GameOptionsActivity<RoomsGame, RoomsDocument, RoomsGameMove, RoomsGameState>() {
    private val document: RoomsDocument by inject()
    override val doc get() = document
}

class RoomsHelpActivity : GameHelpActivity<RoomsGame, RoomsDocument, RoomsGameMove, RoomsGameState>() {
    private val document: RoomsDocument by inject()
    override val doc get() = document
}