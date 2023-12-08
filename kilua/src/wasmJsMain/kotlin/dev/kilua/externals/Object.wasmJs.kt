@file:Suppress("TooManyFunctions")
/*
 * Copyright (c) 2023 Robert Jaros
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.kilua.externals

import dev.kilua.utils.toArray

@JsFun("() => ( {} )")
public actual external fun obj(): JsAny

/**
 * Helper function for creating JavaScript objects with given type.
 */
public inline fun <T : JsAny> obj(init: T.() -> Unit): T {
    return (obj().unsafeCast<T>()).apply(init)
}

@JsFun("(obj, key, value) => ( obj[key] = value )")
private external fun objSet(obj: JsAny, key: String, value: JsAny)

@JsFun("(obj, key) => ( obj[key] )")
private external fun objGet(obj: JsAny, key: String): JsAny?

/**
 * Operator to set property on JS Object
 */
public actual operator fun JsAny.set(key: String, value: JsAny) {
    objSet(this, key, value)
}

/**
 * Operator to get property from JS Object
 */
public actual operator fun JsAny.get(key: String): JsAny? {
    return objGet(this, key)?.unsafeCast()
}

@JsFun("(obj) => ( Object.keys(obj) )")
private external fun jsKeys(obj: JsAny): JsArray<JsString>

/**
 * Get the list of keys from JS Object
 */
public actual fun keys(o: JsAny): List<String> {
    return jsKeys(o).toArray().asList().map { it.toString() }
}

@JsFun("(target, source) => ( Object.assign(target, source) )")
private external fun jsAssign(target: JsAny, source: JsAny)

/**
 * Copies all properties from source object to the target object
 */
public actual fun assign(target: JsAny, source: JsAny) {
    jsAssign(target, source)
}
