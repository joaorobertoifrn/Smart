package br.edu.ifrn.smart.utils

import java.util.*

object ObjectParcer {

    private val map = LinkedHashMap<String, Any>()

    fun putObject(key: String, obj: Any) {
        map.put(key, obj)
    }

    fun getObject(key: String): Any? {
        val obj = map.get(key)
        map.remove(key)
        return obj
    }
}