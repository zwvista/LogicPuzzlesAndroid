package com.zwstudio.logicpuzzlesandroid.puzzles.culturetrip

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class CultureTripMainActivity : GameMainActivity<CultureTripGame, CultureTripDocument, CultureTripGameMove, CultureTripGameState>() {
    private val document: CultureTripDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, CultureTripOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, CultureTripGameActivity::class.java))
    }
}

class CultureTripOptionsActivity : GameOptionsActivity<CultureTripGame, CultureTripDocument, CultureTripGameMove, CultureTripGameState>() {
    private val document: CultureTripDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class CultureTripHelpActivity : GameHelpActivity<CultureTripGame, CultureTripDocument, CultureTripGameMove, CultureTripGameState>() {
    private val document: CultureTripDocument by inject()
    override val doc get() = document
}