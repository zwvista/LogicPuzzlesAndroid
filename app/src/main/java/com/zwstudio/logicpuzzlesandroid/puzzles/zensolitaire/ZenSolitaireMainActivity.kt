package com.zwstudio.logicpuzzlesandroid.puzzles.zensolitaire

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class ZenSolitaireMainActivity : GameMainActivity<ZenSolitaireGame, ZenSolitaireDocument, ZenSolitaireGameMove, ZenSolitaireGameState>() {
    private val document: ZenSolitaireDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, ZenSolitaireOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, ZenSolitaireGameActivity::class.java))
    }
}

class ZenSolitaireOptionsActivity : GameOptionsActivity<ZenSolitaireGame, ZenSolitaireDocument, ZenSolitaireGameMove, ZenSolitaireGameState>() {
    private val document: ZenSolitaireDocument by inject()
    override val doc get() = document
}

class ZenSolitaireHelpActivity : GameHelpActivity<ZenSolitaireGame, ZenSolitaireDocument, ZenSolitaireGameMove, ZenSolitaireGameState>() {
    private val document: ZenSolitaireDocument by inject()
    override val doc get() = document
}