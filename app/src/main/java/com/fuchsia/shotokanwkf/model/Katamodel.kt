package com.fuchsia.shotokanwkf.model

class katamodel {
    var name: String? = null
    var pic: String? = null
    var url: String? = null

    internal constructor() {}
    constructor(Name: String?, Pic: String?, URL: String?) {
        name = Name
        pic = Pic
        url = URL
    }
}