package br.edu.ifsp.scl.todolistsdm.controller

import android.content.Context
import androidx.room.Room
import br.edu.ifsp.scl.todolistsdm.model.dao.TarefaDao
import br.edu.ifsp.scl.todolistsdm.model.database.ToDoListDatabase
import br.edu.ifsp.scl.todolistsdm.model.entity.Tarefa
import br.edu.ifsp.scl.todolistsdm.view.TodoListViewInterface
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TodoListPresenter(private val view: TodoListViewInterface) {

    /* Também conhece o modelo, mas por injeção de dependência, isso não é um problema */
    private val model: TarefaDao = Room.databaseBuilder(
        (view as Context),
        ToDoListDatabase::class.java,
        ToDoListDatabase.Constantes.DB_NAME
    ).build().getTarefaDao()

    fun buscarTarefas() {
        GlobalScope.launch {
            val listaTarefas = model.recuperarTarefas().toMutableList()
            view.setTarefas(listaTarefas)
        }
    }

    fun apagarTarefa(tarefa: Tarefa){
        GlobalScope.launch {
            model.removerTarefa(tarefa)
        }
    }

    fun apagarTarefas(vararg tarefa: Tarefa){
        GlobalScope.launch {
            model.removerTarefas(*tarefa)
        }
    }

    fun salvarTarefa(tarefa: Tarefa) {
        GlobalScope.launch {
            val id = model.inserirTarefa(tarefa)
            val tarefaRetorno = model.recuperaTarefa(id.toInt())

            view.setRetorno(tarefaRetorno)
        }
    }

    fun alterarTarefa(tarefa: Tarefa) {
        GlobalScope.launch { model.atualizarTarefa(tarefa) }
        view.setRetorno(tarefa)
    }
}