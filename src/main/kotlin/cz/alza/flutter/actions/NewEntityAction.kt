package cz.alza.flutter.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.ui.InputValidatorEx
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.PsiDirectory
import cz.alza.flutter.models.Bloc
import cz.alza.flutter.utils.FlutterProjectUtils
import cz.alza.flutter.utils.TemplateBuilder

class NewEntityAction : AnAction(IconLoader.getIcon("/icons/bloc_icon16.png")) {

	override fun actionPerformed(event: AnActionEvent) {
		val project = event.project
				?: throw IllegalStateException("Cannot find project")
		val projectName = FlutterProjectUtils.readProjectName(project)
				?: throw IllegalStateException("Cannot find Flutter project name")

		val name = Messages.showInputDialog(
				"Enter a name for the entity (PascalCase)",
				"New Alza Entity",
				null,
				null,
				SimpleClassNameInputValidator()
		)

		if (name?.isBlank() != false || event.getData(LangDataKeys.PSI_ELEMENT) !is PsiDirectory) {
			return
		}

		val bloc = Bloc.build(name, projectName)

		val directory = event.getData(LangDataKeys.PSI_ELEMENT) as PsiDirectory
		if (directory.findSubdirectory(bloc.name) != null) {
			Messages.showErrorDialog("An entity with the same name already exists", "Alza Entity")
			return
		}

		WriteCommandAction.runWriteCommandAction(event.project) {
			TemplateBuilder.buildEntity(name, project, directory)
		}
	}

	class SimpleClassNameInputValidator : InputValidatorEx {

		override fun checkInput(inputString: String): Boolean {
			return getErrorText(inputString) == null
		}

		override fun canClose(inputString: String?) = true

		override fun getErrorText(inputString: String): String? {
			if (!inputString.matches(Regex("[A-Z][a-zA-Z]+"))) {
				return "Name must be in CamelCase"
			}
			if (inputString.contains("bloc", true)) {
				return "Do not use the word \"bloc\" in the name as it will be automatically added to the name afterwards"
			}
			return null
		}
	}
}