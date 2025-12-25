import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import com.lagradost.cloudstream3.AcraApplication.Companion.getKey
import com.lagradost.cloudstream3.AcraApplication.Companion.setKey

@CloudstreamPlugin
class TokusatsuUltimatePlugin: Plugin() {
    override fun load() {
        // Registers the main API for this plugin
        registerMainAPI(TokusatsuUltimate())
        registerExtractorAPI(TokusatsuUltimate.P2pplay())
    }
    
    override fun setupPreferenceScreen(screen: SettingsScreen) {
        screen.singleChoice(
            key = "tokusatsu_server",
            title = "Select Server",
            entries = arrayOf("toku555.com", "tokuzilla.net"),
            entryValues = arrayOf("toku555.com", "tokuzilla.net"),
            defaultValue = "toku555.com"
        )
    }

    companion object {
        var currentTokusatsuServer: String
            get() = "https://" + (getKey("tokusatsu_server") ?: "toku555.com")
            set(value) {
                setKey("tokusatsu_server", value.removePrefix("https://"))
            }
    }
}