package com.template.utils

import org.json.JSONObject

internal fun <U, T> Map<U ,T>.toJson(): String {
    return JSONObject(this).toString()
}