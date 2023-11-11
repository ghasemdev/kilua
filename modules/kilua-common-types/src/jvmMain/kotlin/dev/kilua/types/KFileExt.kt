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
package dev.kilua.types

import java.io.InputStream
import java.util.*

/**
 *  Decode the byte array from the data uri string contained in the KFile object.
 */
public val KFile.byteArray: ByteArray?
    get() = base64Encoded?.let { Base64.getDecoder().decode(it) }

/**
 *  Decode the input stream from the data uri string contained in the KFile object.
 */
public val KFile.inputStream: InputStream?
    get() = byteArray?.inputStream()

/**
 *  Decode the content from the data uri string contained in the KFile object.
 */
@Suppress("MagicNumber")
public fun KFile.decoded(): Pair<String?, ByteArray?> {
    return content?.split(",", limit = 2)?.let {
        if (it.size == 2) {
            val contentType = it[0].drop(5).split(";")[0]
            val byteArray = Base64.getDecoder().decode(it[1])
            contentType to byteArray
        } else null
    } ?: Pair(null, null)
}
