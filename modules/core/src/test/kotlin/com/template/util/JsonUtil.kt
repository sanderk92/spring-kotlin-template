package com.template.util

import org.json.JSONObject

fun Map<*, *>.asJson() = JSONObject(this).toString()
