package com.micoredu.reader.utils.webdav.http;
object HttpAuth {

    var auth: Auth? = null

    class Auth(val user: String, val pass: String)

}