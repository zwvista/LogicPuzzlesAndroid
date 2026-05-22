package com.zwstudio.logicpuzzlesandroid.puzzles.banquet

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class BanquetMainActivity : GameMainActivity<BanquetGame, BanquetDocument, BanquetGameMove, BanquetGameState>() {
    private val document: BanquetDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, BanquetOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, BanquetGameActivity::class.java))
    }
}

class BanquetOptionsActivity : GameOptionsActivity<BanquetGame, BanquetDocument, BanquetGameMove, BanquetGameState>() {
    private val document: BanquetDocument by inject()
    override val doc get() = document
}

class BanquetHelpActivity : GameHelpActivity<BanquetGame, BanquetDocument, BanquetGameMove, BanquetGameState>() {
    private val document: BanquetDocument by inject()
    override val doc get() = document
}