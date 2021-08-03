package com.zwstudio.logicpuzzlesandroid.puzzles.gardener

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class GardenerMainActivity : GameMainActivity<GardenerGame, GardenerDocument, GardenerGameMove, GardenerGameState>() {
    private val document: GardenerDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, GardenerOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, GardenerGameActivity::class.java))
    }
}

class GardenerOptionsActivity : GameOptionsActivity<GardenerGame, GardenerDocument, GardenerGameMove, GardenerGameState>() {
    private val document: GardenerDocument by inject()
    override val doc get() = document
}

class GardenerHelpActivity : GameHelpActivity<GardenerGame, GardenerDocument, GardenerGameMove, GardenerGameState>() {
    private val document: GardenerDocument by inject()
    override val doc get() = document
}