package com.edu.sun.oereminder.utils

const val HTML_END_LINE = "<br>"
const val END_LINE = "\n"
const val END_LINE_CHAR = '\n'
const val CHAR_QUOTATION_MARK = '"'
const val ASTERISK = "*"

fun String.remove(regexString: String) = replace(regexString.toRegex(), "")

fun String.replaceWithRegex(regexString: String, newString: String) =
    replace(regexString.toRegex(), newString)

fun String.showLast(size: Int) = ASTERISK.repeat(length - size) + substring(length - size)

fun String.normalizeEndLine() = replace(HTML_END_LINE.toRegex(), END_LINE).trimEndLine()

fun String.trimEndLine() = trimStart(END_LINE_CHAR).trimEnd(END_LINE_CHAR)

fun String.trimQuotationMark() = trimStart(CHAR_QUOTATION_MARK).trimEnd(CHAR_QUOTATION_MARK)

fun String.toHtmlEndLine() = replace(END_LINE.toRegex(), HTML_END_LINE)

fun String?.capitalize() = this?.firstOrNull()?.toUpperCase()?.plus(substring(1))
