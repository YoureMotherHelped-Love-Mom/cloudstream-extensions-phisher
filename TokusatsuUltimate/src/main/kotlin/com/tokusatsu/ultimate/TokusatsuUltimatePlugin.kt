import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import com.lagradost.cloudstream3.AcraApplication.Companion.getKey
import com.lagradost.cloudstream3.AcraApplication.Companion.setKey

@CloudstreamPlugin
class TokusatsuUltimatePlugin: Plugin() {
    override fun load() {
        registerMainAPI(TokusatsuUltimate())
        registerExtractorAPI(TokusatsuUltimate.P2pplay())
    }

    companion object {
        var currentTokusatsuServer: String
            get() = getKey("TOKUSATSU_CURRENT_SERVER") ?: "https://toku555.com"
            set(value) {
                setKey("TOKUSATSU_CURRENT_SERVER", value)
            }
    }
}