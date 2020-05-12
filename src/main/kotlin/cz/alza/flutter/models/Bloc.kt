package cz.alza.flutter.models

import cz.alza.flutter.utils.toSnakeCase

data class Bloc(
		val name: String,
		val className: String,
		val projectName: String,
		val stateFilename: String,
		val blocFilename: String,
		val eventFilename: String,
		val locsFileName: String,
		val blocGFileName: String
) {

	companion object {
		fun build(name: String, projectName: String): Bloc {
			val nameSnakeCase = name.toSnakeCase()
			return Bloc(
					nameSnakeCase,
					name,
					projectName,
					"${nameSnakeCase}_state.dart",
					"${nameSnakeCase}_bloc.dart",
					"${nameSnakeCase}_event.dart",
					"locs.dart",
					"${nameSnakeCase}_bloc.g.dart"
			)
		}
	}
}