package com.estasvegano.android.estasvegano.data.web


object UrlConstants {

    const val ID_REPLACEMENT = "id"
    const val BASE_URL = "http://evegano.free-node.ru/"
    const val CHECK = "/api/v1.0/check"
    const val ADD = "/api/v1.0/add"
    const val CATEGORIES = "/api/v1.0/categories"
    const val ADD_PRODUCER = "/api/v1.0/add-producer"

    const private val ID_REPLACEMENT_BRACKETS = "{$ID_REPLACEMENT}"

    const val ADD_IMAGE = "/api/v1.0/add/$ID_REPLACEMENT_BRACKETS/photo"

    const val COMPLAIN = "/api/v1.0/$ID_REPLACEMENT_BRACKETS/complain"

    const val PRODUCERS = "/api/v1.0/producers/"

    const val SUB_CATEGORIES = "/api/v1.0/categories/" + ID_REPLACEMENT_BRACKETS

    const val PRODUCERS_TITLE_QUERY = "title"
}
