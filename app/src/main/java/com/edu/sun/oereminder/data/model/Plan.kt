package com.edu.sun.oereminder.data.model

data class Plan(var messId: Int, var date: String, var plan: String) {
    override fun toString(): String {
        return "[info]\n" +
                "[title]1. Plan $date[/title]\n" +
                "$plan\n" +
                "[/info]"
    }
}
