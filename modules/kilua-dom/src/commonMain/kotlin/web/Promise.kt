/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package web

@Suppress("EXPECTED_EXTERNAL_DECLARATION")
public expect external class Promise<T : JsAny?>(executor: (resolve: (T) -> Unit, reject: (JsAny) -> Unit) -> Unit) :
    JsAny {
    public fun <S : JsAny?> then(onFulfilled: ((T) -> S)?): Promise<S>

    public fun <S : JsAny?> then(onFulfilled: ((T) -> S)?, onRejected: ((JsAny) -> S)?): Promise<S>

    public fun <S : JsAny?> catch(onRejected: (JsAny) -> S): Promise<S>
    public fun finally(onFinally: () -> Unit): Promise<T>

    public companion object {
        public fun <S : JsAny?> all(promise: JsArray<out Promise<S>>): Promise<JsArray<out S>>
        public fun <S : JsAny?> race(promise: JsArray<out Promise<S>>): Promise<S>
        public fun reject(e: JsAny): Promise<Nothing>
        public fun <S : JsAny?> resolve(e: S): Promise<S>
        public fun <S : JsAny?> resolve(e: Promise<S>): Promise<S>
    }

}
