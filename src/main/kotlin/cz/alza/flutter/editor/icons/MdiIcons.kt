package cz.alza.flutter.editor.icons

import cz.alza.flutter.editor.AbstractFlutterIcons
import cz.alza.flutter.utils.toSnakeCase
import javax.swing.Icon

object MdiIcons : AbstractFlutterIcons<MdiIcons>("material_community", MdiIcons::class.java) {

	override fun getIcon(name: String?): Icon? {
		println("MdiIcons Name: $name -> ${name?.toSnakeCase()}")
		return super.getIcon(name?.toSnakeCase())
	}
}