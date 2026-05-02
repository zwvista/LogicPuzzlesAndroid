package com.zwstudio.logicpuzzlesandroid.puzzles.farmer

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class FarmerMainActivity : GameMainActivity<FarmerGame, FarmerDocument, FarmerGameMove, FarmerGameState>() {
    private val document: FarmerDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, FarmerOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, FarmerGameActivity::class.java))
    }
}

class FarmerOptionsActivity : GameOptionsActivity<FarmerGame, FarmerDocument, FarmerGameMove, FarmerGameState>() {
    private val document: FarmerDocument by inject()
    override val doc get() = document
}

class FarmerHelpActivity : GameHelpActivity<FarmerGame, FarmerDocument, FarmerGameMove, FarmerGameState>() {
    private val document: FarmerDocument by inject()
    override val doc get() = document
}