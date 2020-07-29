package com.edu.sun.oereminder.data.model

data class Report(
    var messId: Int,
    var plan: String,
    var actual: String,
    var next: String,
    var issue: String
) {
    override fun toString(): String {
        return "[info]\n" +
                "[title]1. Plan:[/title]\n" +
                "$plan\n" +
                "[title]2. Actual:[/title]\n" +
                "$actual\n" +
                "[title]3. Next:[/title]\n" +
                "$next\n" +
                "[title]4. Issue:[/title]\n" +
                "$issue\n" +
                "[/info]"
    }
}
