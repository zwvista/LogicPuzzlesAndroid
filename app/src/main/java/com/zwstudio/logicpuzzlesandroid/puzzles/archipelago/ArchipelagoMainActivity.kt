package com.zwstudio.logicpuzzlesandroid.puzzles.archipelago

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class ArchipelagoMainActivity : GameMainActivity<ArchipelagoGame, ArchipelagoDocument, ArchipelagoGameMove, ArchipelagoGameState>() {
    private val document: ArchipelagoDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, ArchipelagoOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, ArchipelagoGameActivity::class.java))
    }
}

class ArchipelagoOptionsActivity : GameOptionsActivity<ArchipelagoGame, ArchipelagoDocument, ArchipelagoGameMove, ArchipelagoGameState>() {
    private val document: ArchipelagoDocument by inject()
    override val doc get() = document
}

class ArchipelagoHelpActivity : GameHelpActivity<ArchipelagoGame, ArchipelagoDocument, ArchipelagoGameMove, ArchipelagoGameState>() {
    private val document: ArchipelagoDocument by inject()
    override val doc get() = document
}