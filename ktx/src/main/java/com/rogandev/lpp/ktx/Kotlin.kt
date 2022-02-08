package com.rogandev.lpp.ktx

// Result extensions

inline fun <A, B> Result<List<A>>.mapList(mapper: (A) -> B): Result<List<B>> =
    map { it.map(mapper) }

fun <A, B> Result<A>.andThen(then: (A) -> Result<B>): Result<B> =
    mapCatching { then(it).getOrThrow() }
