package br.edu.ifrn.smart.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import br.edu.ifrn.smart.R
import br.edu.ifrn.smart.models.EquipmentObj
import br.edu.ifrn.smart.utils.ConnectionManager
import br.edu.ifrn.smart.utils.ObjectParcer
import com.pawegio.kandroid.e
import com.pawegio.kandroid.textWatcher
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_add_equipment.*

class AddEditEquipmentActivity : AppCompatActivity() {

    companion object {
        val EQUIPMENT = "edit_equipment"
    }

    private var equipment: EquipmentObj? = null
    private var topic: String? = null
    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_equipment)
        setUpTextWatchers()

        equipment = ObjectParcer.getObject(EQUIPMENT) as EquipmentObj?

        if (equipment == null) {
            addButton.setOnClickListener { addEquipment() }

            addButton.visibility = View.VISIBLE
            deleteButton.visibility = View.GONE
            saveButton.visibility = View.GONE
        } else {
            addButton.visibility = View.GONE
            deleteButton.visibility = View.VISIBLE
            saveButton.visibility = View.VISIBLE

            deleteButton.setOnClickListener { deleteEquipment() }
            saveButton.setOnClickListener { editEquipment() }

            etName.setText(equipment?.name)
            etTopic.setText(equipment?.topic)
        }
    }

    private fun editEquipment() {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        if (topic != null && topic != equipment?.topic) {
            equipment?.deleteFromRealm()
            realm.commitTransaction()
            addEquipment()
            return
        }
        if (name != null) equipment?.name = name!!
        realm.commitTransaction()
        finish()
    }

    private fun deleteEquipment() {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        equipment?.deleteFromRealm()
        realm.commitTransaction()
        finish()
    }

    private fun addEquipment() {
        ConnectionManager.client?.subscribe(topic, 0) ?: e("Erro ao subscribing no topico")
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val equip = realm.createObject(EquipmentObj::class.java, topic!!)
        equip.name = name!!
        realm.commitTransaction()
        finish()
    }

    private fun setUpTextWatchers() {
        etTopic.textWatcher {
            afterTextChanged { text ->
                topic = text.toString().trim()
                shouldEnableButton()
            }
        }
        etName.textWatcher {
            afterTextChanged { text ->
                name = text.toString().trim()
                shouldEnableButton()
            }
        }
    }

    private fun shouldEnableButton() {
        addButton.isEnabled = !topic.isNullOrEmpty() && !name.isNullOrEmpty()
    }
}
