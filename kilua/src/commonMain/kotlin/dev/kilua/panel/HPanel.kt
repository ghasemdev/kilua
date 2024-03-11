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

package dev.kilua.panel

import androidx.compose.runtime.Composable
import dev.kilua.core.ComponentBase
import dev.kilua.html.AlignItems
import dev.kilua.html.CssSize
import dev.kilua.html.Div
import dev.kilua.html.FlexWrap
import dev.kilua.html.JustifyContent

/**
 * Creates a container with a horizontal layout.
 *
 * @param flexWrap the optional flex wrap
 * @param justifyContent the optional flexbox content justification
 * @param alignItems the optional flexbox items alignment
 * @param gap the optional gap between columns
 * @param rowGap the optional gap between rows
 * @param className the optional CSS class name
 * @param content the content of the component
 * @return the created [dev.kilua.html.Div] component
 */
@Composable
public fun ComponentBase.hPanel(
    flexWrap: FlexWrap? = null,
    justifyContent: JustifyContent? = null,
    alignItems: AlignItems? = null,
    gap: CssSize? = null,
    rowGap: CssSize? = null,
    className: String? = null,
    content: @Composable Div.() -> Unit,
): Div {
    return flexPanel(
        null,
        flexWrap,
        justifyContent,
        alignItems,
        null,
        rowGap,
        gap,
        className,
        content
    )
}
