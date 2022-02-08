package com.rogandev.lpp.ktx

// Result

inline fun <A, B> Result<Iterable<A>>.mapIterable(mapper: (A) -> B) = map { it.map(mapper) }
