package com.zwstudio.logicpuzzlesandroid.puzzles.wishsandwich

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class WishSandwichGameActivity : GameGameActivity<WishSandwichGame, WishSandwichDocument, WishSandwichGameMove, WishSandwichGameState>() {
    private val document: WishSandwichDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = WishSandwichGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, WishSandwichHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        WishSandwichGame(level.layout, this, doc)
}