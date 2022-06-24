package com.example.activediet.utilities.run

import android.content.Context
import android.view.LayoutInflater
import com.example.activediet.data.IngredientSearch
import com.example.activediet.data.Run
import com.example.activediet.databinding.MarkerViewBinding
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.util.*

class CustomMarkerView(
    val cals: List<Float>,
    c: Context,
    layoutId: Int
) : MarkerView(c, layoutId) {




    override fun getOffset(): MPPointF {
        return MPPointF(-width / 2f, -height.toFloat())
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)


        if(e == null)  return

        val id = e.x.toInt()
        val cal = cals[id]

    }
}