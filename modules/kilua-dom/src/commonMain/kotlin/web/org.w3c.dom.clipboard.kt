/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

// NOTE: THIS FILE IS AUTO-GENERATED, DO NOT EDIT!
// See github.com/kotlin/dukat for details

@file:Suppress(
    "NO_EXPLICIT_VISIBILITY_IN_API_MODE",
    "NO_EXPLICIT_RETURN_TYPE_IN_API_MODE"
) // TODO: Fix in dukat: https://github.com/Kotlin/dukat/issues/124

package web.dom.clipboard

import web.JsAny
import web.JsString
import web.dom.DataTransfer
import web.dom.EventInit
import web.dom.events.Event
import web.dom.events.EventTarget
import web.Promise

public external interface ClipboardEventInit : EventInit, JsAny {
    var clipboardData: DataTransfer? /* = null */

}

/**
 * Exposes the JavaScript [ClipboardEvent](https://developer.mozilla.org/en/docs/Web/API/ClipboardEvent) to Kotlin
 */
public external open class ClipboardEvent(type: String, eventInitDict: ClipboardEventInit) : Event,
    JsAny {
    open val clipboardData: DataTransfer?

    companion object {
        val NONE: Short
        val CAPTURING_PHASE: Short
        val AT_TARGET: Short
        val BUBBLING_PHASE: Short
    }
}

/**
 * Exposes the JavaScript [Clipboard](https://developer.mozilla.org/en/docs/Web/API/Clipboard) to Kotlin
 */
public external abstract class Clipboard : EventTarget, JsAny {
    fun read(): Promise<*>
    fun readText(): Promise<*>
    fun write(data: DataTransfer): Promise<Nothing?>
    fun writeText(data: String): Promise<Nothing?>
}

public external interface ClipboardPermissionDescriptor : JsAny {
    var allowWithoutGesture: Boolean? /* = false */

}
