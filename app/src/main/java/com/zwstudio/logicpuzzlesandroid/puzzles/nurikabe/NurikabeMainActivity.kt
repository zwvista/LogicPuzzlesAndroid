package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class NurikabeMainActivity : GameMainActivity<NurikabeGame, NurikabeDocument, NurikabeGameMove, NurikabeGameState>() {
    private val document: NurikabeDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, NurikabeOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, NurikabeGameActivity::class.java))
    }
}

class NurikabeOptionsActivity : GameOptionsActivity<NurikabeGame, NurikabeDocument, NurikabeGameMove, NurikabeGameState>() {
    private val document: NurikabeDocument by inject()
    override val doc get() = document
}

class NurikabeHelpActivity : GameHelpActivity<NurikabeGame, NurikabeDocument, NurikabeGameMove, NurikabeGameState>() {
    private val document: NurikabeDocument by inject()
    override val doc get() = document
}