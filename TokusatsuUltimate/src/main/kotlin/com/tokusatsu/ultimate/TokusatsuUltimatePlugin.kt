package com.tokusatsu.ultimate

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.lagradost.cloudstream3.AcraApplication.Companion.getKey
import com.lagradost.cloudstream3.AcraApplication.Companion.setKey
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

enum class TokusatsuServerList(val link: Pair<String, Boolean>) {
    TOKU555_COM("https://toku555.com" to true),
    TOKUZILLA_NET("https://tokuzilla.net" to true),
}

@CloudstreamPlugin
class TokusatsuUltimatePlugin: Plugin() {
    override fun load(context: Context) {
        // Registers the main API for this plugin
        registerMainAPI(TokusatsuUltimate())
        registerExtractorAPI(TokusatsuUltimate.P2pplay())
        this.openSettings = openSettings@{ ctx ->
            val activity = ctx as AppCompatActivity
            val frag = TokusatsuBottomFragment(this)
            frag.show(activity.supportFragmentManager, "")
        }
    }

    companion object {
        var currentTokusatsuServer: String
            get() = getKey("TOKUSATSU_CURRENT_SERVER") ?: TokusatsuServerList.TOKU555_COM.link.first
            set(value) {
                setKey("TOKUSATSU_CURRENT_SERVER", value)
            }
    }
}