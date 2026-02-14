package com.zwstudio.logicpuzzlesandroid.puzzles.cloudsandclears

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class CloudsAndClearsMainActivity : GameMainActivity<CloudsAndClearsGame, CloudsAndClearsDocument, CloudsAndClearsGameMove, CloudsAndClearsGameState>() {
    private val document: CloudsAndClearsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, CloudsAndClearsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, CloudsAndClearsGameActivity::class.java))
    }
}

class CloudsAndClearsOptionsActivity : GameOptionsActivity<CloudsAndClearsGame, CloudsAndClearsDocument, CloudsAndClearsGameMove, CloudsAndClearsGameState>() {
    private val document: CloudsAndClearsDocument by inject()
    override val doc get() = document
}

class CloudsAndClearsHelpActivity : GameHelpActivity<CloudsAndClearsGame, CloudsAndClearsDocument, CloudsAndClearsGameMove, CloudsAndClearsGameState>() {
    private val document: CloudsAndClearsDocument by inject()
    override val doc get() = document
}
