package com.futurecode.ghostfinderradardetector.utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.futurecode.ghostfinderradardetector.R

class PermissionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val icon: ImageView
    private val iconBg: View
    private val title: TextView
    private val desc: TextView
    private val toggle: SwitchCompat

    init {
        LayoutInflater.from(context).inflate(R.layout.view_permission, this, true)

        icon = findViewById(R.id.ivIcon)
        iconBg = findViewById(R.id.vIconBg)
        title = findViewById(R.id.tvTitle)
        desc = findViewById(R.id.tvDesc)
        toggle = findViewById(R.id.switchToggle)

        attrs?.let {
            val ta = context.obtainStyledAttributes(
                it,
                R.styleable.PermissionView,
                0,
                0
            )

            title.text = ta.getString(R.styleable.PermissionView_titleText) ?: ""
            desc.text = ta.getString(R.styleable.PermissionView_descText) ?: ""
            
            ta.getDrawable(R.styleable.PermissionView_iconSrc)?.let { drawable ->
                icon.setImageDrawable(drawable)
            }
            
            ta.getDrawable(R.styleable.PermissionView_iconBg)?.let { drawable ->
                iconBg.background = drawable
            }

            ta.recycle()
        }
    }

    fun isPermissionEnabled(): Boolean = toggle.isChecked

    fun setToggleChecked(isChecked: Boolean) {
        toggle.isChecked = isChecked
    }

    fun setOnToggleChanged(listener: (Boolean) -> Unit) {
        toggle.setOnCheckedChangeListener { _, isChecked ->
            listener(isChecked)
        }
    }

    fun setData(titleText: String, descText: String, iconRes: Int, iconBgRes: Int? = null) {
        title.text = titleText
        desc.text = descText
        icon.setImageResource(iconRes)
        iconBgRes?.let { iconBg.setBackgroundResource(it) }
    }
}