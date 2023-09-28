package com.fuchsia.shotokanwkf.model

class kumitemodel {
    var name: String? = null
    var url: String? = null

    internal constructor() {}

    constructor(Name: String?, URL: String?) {
        name = Name
        url = URL
    }
}