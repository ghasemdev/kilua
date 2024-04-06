/*
 * Copyright (c) 2024 Robert Jaros
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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import app.softwork.routingcompose.HashRouter
import dev.kilua.Application
import dev.kilua.KiluaScope
import dev.kilua.compose.root
import dev.kilua.externals.get
import dev.kilua.externals.set
import dev.kilua.form.check.checkBox
import dev.kilua.form.text.Text
import dev.kilua.form.text.text
import dev.kilua.html.button
import dev.kilua.html.div
import dev.kilua.html.footer
import dev.kilua.html.h1
import dev.kilua.html.header
import dev.kilua.html.label
import dev.kilua.html.li
import dev.kilua.html.link
import dev.kilua.html.section
import dev.kilua.html.span
import dev.kilua.html.strong
import dev.kilua.html.ul
import dev.kilua.html.unaryPlus
import dev.kilua.startApplication
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import web.dom.events.FocusEvent
import web.dom.events.KeyboardEvent
import web.dom.events.MouseEvent
import web.localStorage
import web.toJsString

const val ENTER_KEY = 13
const val ESC_KEY = 27

class App : Application() {

    private val json = Json {
        prettyPrint = true
    }

    val _state = mutableStateOf(TodoContract.State(emptyList(), MODE.ALL))
    var state by _state

    override fun start() {
        loadModel()
        root("root") {
            section("todoapp") {
                header("header") {
                    h1 { +"todos" }
                    text(placeholder = "What needs to be done?", className = "new-todo") {
                        autofocus = true
                        onEvent<KeyboardEvent>("keydown") { e ->
                            if (e.keyCode == ENTER_KEY) {
                                addTodo(this.value)
                                this.value = null
                            }
                        }
                    }
                }
                if (state.todos.isNotEmpty()) {
                    section("main") {
                        checkBox(state.areAllCompleted(), className = "toggle-all") {
                            id = "toggle-all"
                            onClick {
                                processInput(TodoContract.Inputs.ToggleAll)
                            }
                        }
                        label("toggle-all") {
                            +"Mark all as complete"
                        }
                        ul("todo-list") {
                            when (state.mode) {
                                MODE.ALL -> state.allListIndexed()
                                MODE.ACTIVE -> state.activeListIndexed()
                                MODE.COMPLETED -> state.completedListIndexed()
                            }.forEach { (index, todo) ->
                                li(className = if (todo.completed) "completed" else null) {
                                    lateinit var edit: Text
                                    div("view") {
                                        checkBox(todo.completed, className = "toggle") {
                                            onClick {
                                                processInput(TodoContract.Inputs.ToggleActive(index))
                                            }
                                        }
                                        label {
                                            +todo.title
                                            onEvent<MouseEvent>("dblclick") {
                                                this@li.element.classList.add("editing")
                                                edit.value = todo.title
                                                edit.focus()
                                            }
                                        }
                                        button("", className = "destroy") {
                                            onClick {
                                                processInput(TodoContract.Inputs.Delete(index))
                                            }
                                        }
                                    }
                                    edit = text(className = "edit") {
                                        onEvent<FocusEvent>("blur") {
                                            if (this@li.element.classList.contains("editing")) {
                                                this@li.element.classList.remove("editing")
                                                editTodo(index, this.value)
                                            }
                                        }
                                        onEvent<KeyboardEvent>("keydown") { e ->
                                            if (e.keyCode == ENTER_KEY) {
                                                editTodo(index, this.value)
                                                this@li.element.classList.remove("editing")
                                            }
                                            if (e.keyCode == ESC_KEY) {
                                                this@li.element.classList.remove("editing")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    footer("footer") {
                        val itemsLeftString = if (state.activeList().size == 1) " item left" else " items left"
                        span("todo-count") {
                            strong {
                                +"${state.activeList().size}"
                            }
                            +itemsLeftString
                        }
                        ul(className = "filters") {
                            li {
                                link("#/", "All", className = if (state.mode == MODE.ALL) "selected" else null)
                            }
                            li {
                                link(
                                    "#/active", "Active",
                                    className = if (state.mode == MODE.ACTIVE) "selected" else null
                                )
                            }
                            li {
                                link(
                                    "#/completed", "Completed",
                                    className = if (state.mode == MODE.COMPLETED) "selected" else null
                                )
                            }
                        }
                        if (state.completedList().isNotEmpty()) {
                            button("Clear completed", className = "clear-completed") {
                                onClick {
                                    processInput(TodoContract.Inputs.ClearCompleted)
                                }
                            }
                        }
                    }
                }
            }
            HashRouter(initPath = "/") {
                route("/") {
                    processInput(TodoContract.Inputs.ShowAll)
                }
                route("/active") {
                    processInput(TodoContract.Inputs.ShowActive)
                }
                route("/completed") {
                    processInput(TodoContract.Inputs.ShowCompleted)
                }
            }
        }
        snapshotFlow {
            _state.value
        }.onEach {
            saveModel()
        }.launchIn(KiluaScope)
    }

    private fun processInput(input: TodoContract.Inputs) {
        state = handleInput(state, input)
    }

    private fun addTodo(value: String?) {
        val v = value?.trim() ?: ""
        if (v.isNotEmpty()) {
            processInput(TodoContract.Inputs.Add(Todo(false, v)))
        }
    }

    private fun editTodo(index: Int, value: String?) {
        val v = value?.trim() ?: ""
        if (v.isNotEmpty()) {
            processInput(TodoContract.Inputs.ChangeTitle(index, v))
        } else {
            processInput(TodoContract.Inputs.Delete(index))
        }
    }

    private fun loadModel() {
        localStorage["todos-kilua"]?.let {
            processInput(
                TodoContract.Inputs.Load(
                    Json.decodeFromString(
                        ListSerializer(Todo.serializer()),
                        it.toString()
                    )
                )
            )
        }
    }

    private fun saveModel() {
        val jsonString = json.encodeToString(ListSerializer(Todo.serializer()), state.todos)
        localStorage["todos-kilua"] = jsonString.toJsString()
    }
}

fun main() {
    startApplication(::App)
}
