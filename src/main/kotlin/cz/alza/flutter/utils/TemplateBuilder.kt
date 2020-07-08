package cz.alza.flutter.utils

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import cz.alza.flutter.models.Bloc
import java.util.*

object TemplateBuilder {

	enum class Template {
		Event, State, Bloc, Locs, BlocG
	}

	private object Properties {
		const val Name = "NAME"
		const val ProjectName = "PROJECT_NAME"
		const val ClassName = "CLASS_NAME"
	}

	fun build(bloc: Bloc, project: Project, destinationDirectory: PsiDirectory) {
		val manager = FileTemplateManager.getInstance(project)
		val properties = buildProperties(manager.defaultProperties, bloc)

		mapTemplates(bloc).forEach { template ->
			buildTemplate(manager, template.key.name, template.value, properties, destinationDirectory)
		}

		val widgetDirectory = destinationDirectory.createSubdirectory("widgets")
		buildTemplate(manager, "widgets", "widgets.dart", properties, widgetDirectory)
		buildTemplate(manager, "bloc_widget", properties.getProperty("NAME") + "_widget.dart", properties, widgetDirectory)

		val dataDirectory = destinationDirectory.createSubdirectory("data")
		buildTemplate(manager, "blank", "data.dart", properties, dataDirectory)

		val entitiesDirectory = destinationDirectory.createSubdirectory("entities")
		buildTemplate(manager, "blank", "entities.dart", properties, entitiesDirectory)
	}

	fun buildEntity(name: String, project: Project, destinationDirectory: PsiDirectory) {
		val manager = FileTemplateManager.getInstance(project)
		val properties = buildProperties(manager.defaultProperties, name)

		buildTemplate(manager, "entity", properties.getProperty("NAME"), properties, destinationDirectory)
		buildTemplate(manager, "entityg", properties.getProperty("NAME") + ".g", properties, destinationDirectory)
	}

	private fun buildTemplate(manager: FileTemplateManager, templateName: String, fileName: String, properties: java.util.Properties, destinationDirectory: PsiDirectory) {
		val fileTemplate = manager.getInternalTemplate(templateName.toLowerCase())
		FileTemplateUtil.createFromTemplate(fileTemplate, fileName, properties, destinationDirectory)
	}

	private fun mapTemplates(bloc: Bloc) = Template.values().associate {
		when (it) {
			Template.State -> Pair(it, bloc.stateFilename)
			Template.Event -> Pair(it, bloc.eventFilename)
			Template.Bloc -> Pair(it, bloc.blocFilename)
			Template.Locs -> Pair(it, bloc.locsFileName)
			Template.BlocG -> Pair(it, bloc.blocGFileName)
		}
	}

	private fun buildProperties(properties: java.util.Properties, bloc: Bloc) = properties.apply {
		put(Properties.Name, bloc.name)
		put(Properties.ProjectName, bloc.projectName)
		put(Properties.ClassName, bloc.className)
	}

	private fun buildProperties(properties: java.util.Properties, name: String) = properties.apply {
		put(Properties.Name, name.toSnakeCase())
		put(Properties.ClassName, name)
	}
}