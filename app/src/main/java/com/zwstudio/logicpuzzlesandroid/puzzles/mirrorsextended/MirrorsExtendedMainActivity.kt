package com.zwstudio.logicpuzzlesandroid.puzzles.mirrorsextended

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class MirrorsExtendedMainActivity : GameMainActivity<MirrorsExtendedGame, MirrorsExtendedDocument, MirrorsExtendedGameMove, MirrorsExtendedGameState>() {
    private val document: MirrorsExtendedDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, MirrorsExtendedOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, MirrorsExtendedGameActivity::class.java))
    }
}

class MirrorsExtendedOptionsActivity : GameOptionsActivity<MirrorsExtendedGame, MirrorsExtendedDocument, MirrorsExtendedGameMove, MirrorsExtendedGameState>() {
    private val document: MirrorsExtendedDocument by inject()
    override val doc get() = document
}

class MirrorsExtendedHelpActivity : GameHelpActivity<MirrorsExtendedGame, MirrorsExtendedDocument, MirrorsExtendedGameMove, MirrorsExtendedGameState>() {
    private val document: MirrorsExtendedDocument by inject()
    override val doc get() = document
}