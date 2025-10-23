package com.zwstudio.logicpuzzlesandroid.puzzles.desertdunes

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class DesertDunesMainActivity : GameMainActivity<DesertDunesGame, DesertDunesDocument, DesertDunesGameMove, DesertDunesGameState>() {
    private val document: DesertDunesDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, DesertDunesOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, DesertDunesGameActivity::class.java))
    }
}

class DesertDunesOptionsActivity : GameOptionsActivity<DesertDunesGame, DesertDunesDocument, DesertDunesGameMove, DesertDunesGameState>() {
    private val document: DesertDunesDocument by inject()
    override val doc get() = document
}

class DesertDunesHelpActivity : GameHelpActivity<DesertDunesGame, DesertDunesDocument, DesertDunesGameMove, DesertDunesGameState>() {
    private val document: DesertDunesDocument by inject()
    override val doc get() = document
}