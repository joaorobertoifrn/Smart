package br.edu.ifrn.smart.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class EquipmentObj constructor(
        @PrimaryKey open var topic: String = "",
        open var name: String = "",
        open var value: String? = null) : RealmObject() {
    override fun toString(): String {
        return "${name} (${topic}) = ${value}"
    }

    companion object {
        val TOPIC: String = "topic"
    }

    fun getValueAsBoolean(): Boolean {
        return !value.isNullOrEmpty() && (value.equals("on", true) || value.equals("true", true))
    }
}