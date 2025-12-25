package com.tokusatsu.ultimate

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lagradost.cloudstream3.mvvm.logError

class TokusatsuBottomFragment(private val plugin: TokusatsuUltimatePlugin) : BottomSheetDialogFragment() {

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Create layout programmatically since we can't rely on resource files
        val layout = createSettingsLayout(inflater)
        return layout
    }

    private fun createSettingsLayout(inflater: LayoutInflater): View {
        val context = inflater.context
        
        // Main container
        val mainContainer = android.widget.LinearLayout(context).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(
                resources.getDimension(androidx.appcompat.R.dimen.abc_list_item_padding_horizontal_material).toInt(),
                resources.getDimension(androidx.appcompat.R.dimen.abc_list_item_padding_horizontal_material).toInt(),
                resources.getDimension(androidx.appcompat.R.dimen.abc_list_item_padding_horizontal_material).toInt(),
                50
            )
        }

        // Header with title and save button
        val headerLayout = android.widget.LinearLayout(context).apply {
            orientation = android.widget.LinearLayout.HORIZONTAL
            val layoutParams = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, 50, 0, 0)
            this.layoutParams = layoutParams
        }

        val title = android.widget.TextView(context).apply {
            text = "TokusatsuUltimate Settings"
            textSize = 20f
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        val saveButton = android.widget.Button(context).apply {
            text = "Save"
            setOnClickListener {
                context?.let { ctx ->
                    AlertDialog.Builder(ctx)
                        .setTitle("Restart App?")
                        .setMessage("Save changes and restart the app?")
                        .setPositiveButton("Yes") { _, _ ->
                            restartApp(ctx)
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                            Toast.makeText(ctx, "Changes saved", Toast.LENGTH_SHORT).show()
                            dismiss()
                        }
                        .show()
                }
            }
        }

        headerLayout.addView(title)
        headerLayout.addView(saveButton)
        mainContainer.addView(headerLayout)

        // Divider
        val divider1 = android.widget.ImageView(context).apply {
            setImageResource(android.R.drawable.divider_horizontal_dark)
            scaleType = android.widget.ImageView.ScaleType.FIT_XY
            setPadding(2, 0, 2, 0)
            val layoutParams = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, 10, 0, 10)
            this.layoutParams = layoutParams
        }
        mainContainer.addView(divider1)

        // Another divider
        val divider2 = android.widget.ImageView(context).apply {
            setImageResource(android.R.drawable.divider_horizontal_dark)
            scaleType = android.widget.ImageView.ScaleType.FIT_XY
            setPadding(2, 0, 2, 0)
            val layoutParams = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, 10, 0, 10)
            this.layoutParams = layoutParams
        }
        mainContainer.addView(divider2)

        // Server selection section
        val serverSection = android.widget.LinearLayout(context).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(0, 0, 0, 10)
        }

        val serverTitle = android.widget.TextView(context).apply {
            text = "TokusatsuUltimate Servers"
            textSize = 17f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, 0, 0, 10)
        }
        serverSection.addView(serverTitle)

        val serverGroup = RadioGroup(context).apply {
            orientation = RadioGroup.VERTICAL
        }
        serverSection.addView(serverGroup)
        mainContainer.addView(serverSection)

        // Add server radio buttons
        TokusatsuServerList.entries.forEach { server ->
            val radioBtn = RadioButton(context).apply {
                text = server.link.first
                isEnabled = server.link.second
                setOnClickListener {
                    TokusatsuUltimatePlugin.currentTokusatsuServer = text.toString()
                    serverGroup.check(id)
                }
            }
            val newId = View.generateViewId()
            radioBtn.id = newId
            serverGroup.addView(radioBtn)
            if (TokusatsuUltimatePlugin.currentTokusatsuServer == server.link.first) {
                serverGroup.check(newId)
            }
        }

        return mainContainer
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    private fun restartApp(context: Context) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        val componentName = intent?.component

        if (componentName != null) {
            val restartIntent = Intent.makeRestartActivityTask(componentName)
            context.startActivity(restartIntent)
            Runtime.getRuntime().exit(0)
        }
    }
}