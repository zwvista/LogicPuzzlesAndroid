package com.zwstudio.logicpuzzlesandroid.puzzles.zengardens

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class ZenGardensMainActivity : GameMainActivity<ZenGardensGame, ZenGardensDocument, ZenGardensGameMove, ZenGardensGameState>() {
    private val document: ZenGardensDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, ZenGardensOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, ZenGardensGameActivity::class.java))
    }
}

class ZenGardensOptionsActivity : GameOptionsActivity<ZenGardensGame, ZenGardensDocument, ZenGardensGameMove, ZenGardensGameState>() {
    private val document: ZenGardensDocument by inject()
    override val doc get() = document
}

class ZenGardensHelpActivity : GameHelpActivity<ZenGardensGame, ZenGardensDocument, ZenGardensGameMove, ZenGardensGameState>() {
    private val document: ZenGardensDocument by inject()
    override val doc get() = document
}