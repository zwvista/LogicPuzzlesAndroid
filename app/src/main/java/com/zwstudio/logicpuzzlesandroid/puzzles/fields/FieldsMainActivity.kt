package com.zwstudio.logicpuzzlesandroid.puzzles.fields

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class FieldsMainActivity : GameMainActivity<FieldsGame, FieldsDocument, FieldsGameMove, FieldsGameState>() {
    private val document: FieldsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, FieldsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, FieldsGameActivity::class.java))
    }
}

class FieldsOptionsActivity : GameOptionsActivity<FieldsGame, FieldsDocument, FieldsGameMove, FieldsGameState>() {
    private val document: FieldsDocument by inject()
    override val doc get() = document
}

class FieldsHelpActivity : GameHelpActivity<FieldsGame, FieldsDocument, FieldsGameMove, FieldsGameState>() {
    private val document: FieldsDocument by inject()
    override val doc get() = document
}