package com.example.emailsplitter

import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

public fun splitEmail(str: String) : Result<Array<String>> {
    val pieces = str.split("@")
    return when(pieces.size){
        2 ->  success(pieces.toTypedArray());
        0 -> failure(Exception("Empty String"))
        1 -> failure(Exception("Missing '@'"))
        else -> failure(Exception("Too many pieces"))
    }
}