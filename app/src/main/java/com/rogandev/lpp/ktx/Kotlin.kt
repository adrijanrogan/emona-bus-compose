package com.rogandev.lpp.ktx

// Result

inline fun <A, B> Result<Iterable<A>>.mapIterable(mapper: (A) -> B): Result<Iterable<B>> =
    map { it.map(mapper) }

fun <A, B> Result<A>.andThen(then: (A) -> Result<B>): Result<B> =
    mapCatching { then(it).getOrThrow() }
