package com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class BWTapaMainActivity : GameMainActivity<BWTapaGame, BWTapaDocument, BWTapaGameMove, BWTapaGameState>() {
    private val document: BWTapaDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, BWTapaOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, BWTapaGameActivity::class.java))
    }
}

class BWTapaOptionsActivity : GameOptionsActivity<BWTapaGame, BWTapaDocument, BWTapaGameMove, BWTapaGameState>() {
    private val document: BWTapaDocument by inject()
    override val doc get() = document
}

class BWTapaHelpActivity : GameHelpActivity<BWTapaGame, BWTapaDocument, BWTapaGameMove, BWTapaGameState>() {
    private val document: BWTapaDocument by inject()
    override val doc get() = document
}