package com.zwstudio.logicpuzzlesandroid.puzzles.gardener.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.data.GardenerDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerGame
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_options)
open class GardenerOptionsActivity : GameOptionsActivity<GardenerGame?, GardenerDocument?, GardenerGameMove?, GardenerGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: GardenerDocument? = null
    override fun doc(): GardenerDocument {
        return document!!
    }
}