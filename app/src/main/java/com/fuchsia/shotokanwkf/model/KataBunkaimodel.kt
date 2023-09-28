package com.fuchsia.shotokanwkf.model

class KataBunkaimodel {
    var name: String? = null
    var url: String? = null

    internal constructor() {}
    constructor(Name: String?, URL: String?) {
        name = Name
        url = URL
    }
}