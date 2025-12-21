package com.zwstudio.logicpuzzlesandroid.puzzles.heliumandiron

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class HeliumAndIronMainActivity : GameMainActivity<HeliumAndIronGame, HeliumAndIronDocument, HeliumAndIronGameMove, HeliumAndIronGameState>() {
    private val document: HeliumAndIronDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, HeliumAndIronOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, HeliumAndIronGameActivity::class.java))
    }
}

class HeliumAndIronOptionsActivity : GameOptionsActivity<HeliumAndIronGame, HeliumAndIronDocument, HeliumAndIronGameMove, HeliumAndIronGameState>() {
    private val document: HeliumAndIronDocument by inject()
    override val doc get() = document
}

class HeliumAndIronHelpActivity : GameHelpActivity<HeliumAndIronGame, HeliumAndIronDocument, HeliumAndIronGameMove, HeliumAndIronGameState>() {
    private val document: HeliumAndIronDocument by inject()
    override val doc get() = document
}