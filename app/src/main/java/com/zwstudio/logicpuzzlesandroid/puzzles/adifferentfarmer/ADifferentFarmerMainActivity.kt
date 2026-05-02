package com.zwstudio.logicpuzzlesandroid.puzzles.adifferentfarmer

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class ADifferentFarmerMainActivity : GameMainActivity<ADifferentFarmerGame, ADifferentFarmerDocument, ADifferentFarmerGameMove, ADifferentFarmerGameState>() {
    private val document: ADifferentFarmerDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, ADifferentFarmerOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, ADifferentFarmerGameActivity::class.java))
    }
}

class ADifferentFarmerOptionsActivity : GameOptionsActivity<ADifferentFarmerGame, ADifferentFarmerDocument, ADifferentFarmerGameMove, ADifferentFarmerGameState>() {
    private val document: ADifferentFarmerDocument by inject()
    override val doc get() = document
}

class ADifferentFarmerHelpActivity : GameHelpActivity<ADifferentFarmerGame, ADifferentFarmerDocument, ADifferentFarmerGameMove, ADifferentFarmerGameState>() {
    private val document: ADifferentFarmerDocument by inject()
    override val doc get() = document
}