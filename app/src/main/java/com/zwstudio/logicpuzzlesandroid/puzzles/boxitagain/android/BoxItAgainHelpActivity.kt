package com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.data.BoxItAgainDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.domain.BoxItAgainGame
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.domain.BoxItAgainGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.domain.BoxItAgainGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_help)
open class BoxItAgainHelpActivity : GameHelpActivity<BoxItAgainGame?, BoxItAgainDocument?, BoxItAgainGameMove?, BoxItAgainGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: BoxItAgainDocument? = null
    override fun doc(): BoxItAgainDocument {
        return document!!
    }
}