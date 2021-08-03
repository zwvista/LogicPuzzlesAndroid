package com.zwstudio.logicpuzzlesandroid.puzzles.northpolefishing

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class NorthPoleFishingMainActivity : GameMainActivity<NorthPoleFishingGame, NorthPoleFishingDocument, NorthPoleFishingGameMove, NorthPoleFishingGameState>() {
    private val document: NorthPoleFishingDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, NorthPoleFishingOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, NorthPoleFishingGameActivity::class.java))
    }
}

class NorthPoleFishingOptionsActivity : GameOptionsActivity<NorthPoleFishingGame, NorthPoleFishingDocument, NorthPoleFishingGameMove, NorthPoleFishingGameState>() {
    private val document: NorthPoleFishingDocument by inject()
    override val doc get() = document
}

class NorthPoleFishingHelpActivity : GameHelpActivity<NorthPoleFishingGame, NorthPoleFishingDocument, NorthPoleFishingGameMove, NorthPoleFishingGameState>() {
    private val document: NorthPoleFishingDocument by inject()
    override val doc get() = document
}
