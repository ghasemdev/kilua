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

package dev.kilua.dropdown

import androidx.compose.runtime.Composable
import dev.kilua.core.IComponent
import dev.kilua.html.ButtonSize
import dev.kilua.html.ButtonStyle
import dev.kilua.html.Div
import dev.kilua.html.IButton
import dev.kilua.html.IDiv
import dev.kilua.html.IUl
import dev.kilua.html.div
import dev.kilua.html.divRef
import dev.kilua.utils.rem

/**
 * Dropdown directions.
 */
public enum class Direction(public val className: String) {
    Dropdown("dropdown"),
    DropdownCenter("dropdown-center"),
    Dropup("btn-group dropup"),
    DropupCenter("dropup-center dropup"),
    DropStart("btn-group dropstart"),
    DropEnd("btn-group dropend")
}

/**
 * Creates a dropdown component, returning a reference.
 *
 * @param label the label of the dropdown button
 * @param icon the icon of the dropdown button
 * @param style the style of the dropdown button
 * @param size the size of the dropdown button
 * @param disabled the disabled state of the dropdown button
 * @param autoClose the auto close state of the dropdown
 * @param arrowVisible the arrow visibility state of the dropdown button
 * @param innerDropDown the inner dropdown state of the dropdown button
 * @param direction the direction of the dropdown
 * @param className the CSS class name
 * @param id the element ID
 * @param content the content of the dropdown
 * @param buttonClassName the CSS class name of the button
 * @param buttonId the ID attribute of the button
 * @param buttonContent the content of the button
 * @param menuEndAlignment the end alignment of the dropdown menu
 * @param menuStartAlignment the start alignment of the dropdown menu
 * @param menuClassName the CSS class name of the dropdown menu
 * @param menuId the ID attribute of the dropdown menu
 * @param menuContent the content of the dropdown menu
 * @return the dropdown component
 */
@Composable
public fun IComponent.dropDownRef(
    label: String? = null,
    icon: String? = null,
    style: ButtonStyle = ButtonStyle.BtnPrimary,
    size: ButtonSize? = null,
    disabled: Boolean? = null,
    autoClose: AutoClose = AutoClose.True,
    arrowVisible: Boolean = true,
    innerDropDown: Boolean = false,
    direction: Direction = Direction.Dropdown,
    className: String? = null,
    id: String? = null,
    content: @Composable IDiv.() -> Unit = {},
    buttonClassName: String? = null,
    buttonId: String? = null,
    buttonContent: @Composable IButton.() -> Unit = {},
    menuEndAlignment: EndAlignment? = null,
    menuStartAlignment: StartAlignment? = null,
    menuClassName: String? = null,
    menuId: String? = null,
    menuContent: @Composable IUl.() -> Unit = {}
): Div {
    return divRef(direction.className % className, id) {
        dropDownButton(
            label,
            icon,
            style,
            size,
            disabled,
            autoClose,
            arrowVisible,
            innerDropDown,
            buttonClassName,
            buttonId,
            buttonContent
        )
        dropDownMenu(menuEndAlignment, menuStartAlignment, menuClassName, menuId, menuContent)
        content()
    }
}

/**
 * Creates a dropdown component.
 *
 * @param label the label of the dropdown button
 * @param icon the icon of the dropdown button
 * @param style the style of the dropdown button
 * @param size the size of the dropdown button
 * @param disabled the disabled state of the dropdown button
 * @param autoClose the auto close state of the dropdown
 * @param arrowVisible the arrow visibility state of the dropdown button
 * @param innerDropDown the inner dropdown state of the dropdown button
 * @param direction the direction of the dropdown
 * @param className the CSS class name
 * @param id the element ID
 * @param content the content of the dropdown
 * @param buttonClassName the CSS class name of the button
 * @param buttonId the ID attribute of the button
 * @param buttonContent the content of the button
 * @param menuEndAlignment the end alignment of the dropdown menu
 * @param menuStartAlignment the start alignment of the dropdown menu
 * @param menuClassName the CSS class name of the dropdown menu
 * @param menuId the ID attribute of the dropdown menu
 * @param menuContent the content of the dropdown menu
 */
@Composable
public fun IComponent.dropDown(
    label: String? = null,
    icon: String? = null,
    style: ButtonStyle = ButtonStyle.BtnPrimary,
    size: ButtonSize? = null,
    disabled: Boolean? = null,
    autoClose: AutoClose = AutoClose.True,
    arrowVisible: Boolean = true,
    innerDropDown: Boolean = false,
    direction: Direction = Direction.Dropdown,
    className: String? = null,
    id: String? = null,
    content: @Composable IDiv.() -> Unit = {},
    buttonClassName: String? = null,
    buttonId: String? = null,
    buttonContent: @Composable IButton.() -> Unit = {},
    menuEndAlignment: EndAlignment? = null,
    menuStartAlignment: StartAlignment? = null,
    menuClassName: String? = null,
    menuId: String? = null,
    menuContent: @Composable IUl.() -> Unit = {}
) {
    div(direction.className % className, id) {
        dropDownButton(
            label,
            icon,
            style,
            size,
            disabled,
            autoClose,
            arrowVisible,
            innerDropDown,
            buttonClassName,
            buttonId,
            buttonContent
        )
        dropDownMenu(menuEndAlignment, menuStartAlignment, menuClassName, menuId, menuContent)
        content()
    }
}
