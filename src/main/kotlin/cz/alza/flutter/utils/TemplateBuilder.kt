package cz.alza.flutter.utils

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import cz.alza.flutter.models.Bloc
object TemplateBuilder {

	private object Properties {
		const val Name = "NAME"
		const val ProjectName = "PROJECT_NAME"
		const val ClassName = "CLASS_NAME"
	}

	fun build(bloc: Bloc, project: Project, destinationDirectory: PsiDirectory) {
		val manager = FileTemplateManager.getInstance(project)
		val properties = buildProperties(manager.defaultProperties, bloc)

		buildTemplate(manager, "alza_state", bloc.stateFilename, properties, destinationDirectory)
		buildTemplate(manager, "alza_event", bloc.eventFilename, properties, destinationDirectory)
		buildTemplate(manager, "alza_bloc", bloc.blocFilename, properties, destinationDirectory)
		buildTemplate(manager, "alza_locs", bloc.locsFileName, properties, destinationDirectory)
		buildTemplate(manager, "alza_blocg", bloc.blocGFileName, properties, destinationDirectory)

		val widgetDirectory = destinationDirectory.createSubdirectory("widgets")
		buildTemplate(manager, "alza_widgets", "widgets.dart", properties, widgetDirectory)
		buildTemplate(manager, "alza_bloc_widget", properties.getProperty("NAME") + "_widget.dart", properties, widgetDirectory)

		val dataDirectory = destinationDirectory.createSubdirectory("data")
		buildTemplate(manager, "blank", "data.dart", properties, dataDirectory)

		val entitiesDirectory = destinationDirectory.createSubdirectory("entities")
		buildTemplate(manager, "blank", "entities.dart", properties, entitiesDirectory)
	}

	fun buildEntity(name: String, project: Project, destinationDirectory: PsiDirectory) {
		val manager = FileTemplateManager.getInstance(project)
		val properties = buildProperties(manager.defaultProperties, name)

		buildTemplate(manager, "alza_entity", properties.getProperty("NAME"), properties, destinationDirectory)
		buildTemplate(manager, "alza_entityg", properties.getProperty("NAME") + ".g", properties, destinationDirectory)
	}

	fun buildDataClass(name: String, project: Project, destinationDirectory: PsiDirectory) {
		val manager = FileTemplateManager.getInstance(project)
		val properties = buildProperties(manager.defaultProperties, name)

		buildTemplate(manager, "alza_struct", properties.getProperty("NAME"), properties, destinationDirectory)
		buildTemplate(manager, "alza_structg", properties.getProperty("NAME") + ".g", properties, destinationDirectory)
	}

	private fun buildTemplate(manager: FileTemplateManager, templateName: String, fileName: String, properties: java.util.Properties, destinationDirectory: PsiDirectory) {
		val fileTemplate = manager.getInternalTemplate(templateName.toLowerCase())
		FileTemplateUtil.createFromTemplate(fileTemplate, fileName, properties, destinationDirectory)
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