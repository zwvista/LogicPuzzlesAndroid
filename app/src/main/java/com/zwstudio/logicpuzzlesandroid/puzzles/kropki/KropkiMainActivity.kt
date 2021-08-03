package com.zwstudio.logicpuzzlesandroid.puzzles.kropki

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class KropkiMainActivity : GameMainActivity<KropkiGame, KropkiDocument, KropkiGameMove, KropkiGameState>() {
    private val document: KropkiDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, KropkiOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, KropkiGameActivity::class.java))
    }
}

class KropkiOptionsActivity : GameOptionsActivity<KropkiGame, KropkiDocument, KropkiGameMove, KropkiGameState>() {
    private val document: KropkiDocument by inject()
    override val doc get() = document
}

class KropkiHelpActivity : GameHelpActivity<KropkiGame, KropkiDocument, KropkiGameMove, KropkiGameState>() {
    private val document: KropkiDocument by inject()
    override val doc get() = document
}