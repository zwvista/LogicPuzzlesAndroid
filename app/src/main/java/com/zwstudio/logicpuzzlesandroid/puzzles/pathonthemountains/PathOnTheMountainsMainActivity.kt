package com.zwstudio.logicpuzzlesandroid.puzzles.pathonthemountains

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class PathOnTheMountainsMainActivity : GameMainActivity<PathOnTheMountainsGame, PathOnTheMountainsDocument, PathOnTheMountainsGameMove, PathOnTheMountainsGameState>() {
    private val document: PathOnTheMountainsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, PathOnTheMountainsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, PathOnTheMountainsGameActivity::class.java))
    }
}

class PathOnTheMountainsOptionsActivity : GameOptionsActivity<PathOnTheMountainsGame, PathOnTheMountainsDocument, PathOnTheMountainsGameMove, PathOnTheMountainsGameState>() {
    private val document: PathOnTheMountainsDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class PathOnTheMountainsHelpActivity : GameHelpActivity<PathOnTheMountainsGame, PathOnTheMountainsDocument, PathOnTheMountainsGameMove, PathOnTheMountainsGameState>() {
    private val document: PathOnTheMountainsDocument by inject()
    override val doc get() = document
}