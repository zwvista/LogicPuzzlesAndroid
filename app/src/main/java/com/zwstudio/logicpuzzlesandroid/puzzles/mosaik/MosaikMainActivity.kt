package com.zwstudio.logicpuzzlesandroid.puzzles.mosaik

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class MosaikMainActivity : GameMainActivity<MosaikGame, MosaikDocument, MosaikGameMove, MosaikGameState>() {
    @Bean
    protected lateinit var document: MosaikDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        MosaikOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        MosaikGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class MosaikOptionsActivity : GameOptionsActivity<MosaikGame, MosaikDocument, MosaikGameMove, MosaikGameState>() {
    @Bean
    protected lateinit var document: MosaikDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class MosaikHelpActivity : GameHelpActivity<MosaikGame, MosaikDocument, MosaikGameMove, MosaikGameState>() {
    @Bean
    protected lateinit var document: MosaikDocument
    override val doc get() = document
}