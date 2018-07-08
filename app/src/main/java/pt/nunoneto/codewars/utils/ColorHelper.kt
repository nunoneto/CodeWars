package pt.nunoneto.codewars.utils

import android.graphics.Color
import android.support.annotation.ColorInt
import java.util.*

object ColorHelper {

    var colorMap = HashMap<String, Int>()
    private val random = Random()

    @ColorInt
    fun getColorForLanguage(language: String) : Int {
        if (colorMap.containsKey(language)) {
            return colorMap[language] ?: 0
        }

        var color = getRandomColor()
        colorMap[language] = color
        return color
    }

    @ColorInt
    fun getRandomColor() : Int {
        return Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255))
    }
}