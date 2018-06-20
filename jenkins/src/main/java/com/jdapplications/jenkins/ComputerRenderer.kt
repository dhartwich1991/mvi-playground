package com.jdapplications.jenkins

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.jdapplications.jenkins.data.Computer
import com.pedrogomez.renderers.Renderer
import kotlinx.android.synthetic.main.jenkins_list_item.view.*

class ComputerRenderer : Renderer<Computer>() {
    override fun inflate(inflater: LayoutInflater, parent: ViewGroup?): View {
        return inflater.inflate(R.layout.jenkins_list_item, parent, false)
    }

    override fun hookListeners(rootView: View?) {
    }

    override fun render() {
        rootView.computerName.text = content.displayName
        rootView.computerDescription.text = content.description
        rootView.computerOffline.text = if (content.offline) "Offline" else "Online"
        if (content.offline) {
            rootView.background = ColorDrawable(ContextCompat.getColor(context, R.color.colorAccent))
        } else {
            rootView.background = ColorDrawable(ContextCompat.getColor(context, android.R.color.background_light))
        }
    }

    override fun setUpView(rootView: View?) {
    }
}