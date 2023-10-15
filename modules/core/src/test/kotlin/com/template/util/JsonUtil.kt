package com.template.util

import org.json.JSONObject

internal fun Map<*, *>.asJson() = JSONObject(this).toString()
