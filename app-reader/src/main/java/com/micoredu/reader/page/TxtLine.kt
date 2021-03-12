package com.micoredu.reader.page

public class TxtLine {

    var charsData: List<TxtChar>? = null

    fun getLineData(): String {
        var linedata = ""
        if (charsData == null) return linedata
        charsData?.let {
            if (it.isEmpty()) return@let linedata
            for (c in it) {
                linedata += c.chardata
            }
        }
        return linedata
    }

    override fun toString(): String {
        return "ShowLine [Linedata=" + getLineData() + "]"
    }

}