package cz.alza.flutter.editor

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.Project
import com.jetbrains.lang.dart.ide.completion.DartCompletionExtension
import com.jetbrains.lang.dart.ide.completion.DartServerCompletionContributor
import cz.alza.flutter.editor.icons.FontAwesomeIcons
import cz.alza.flutter.editor.icons.IonIcons
import cz.alza.flutter.editor.icons.MaterialCommunityIcons
import cz.alza.flutter.editor.icons.MdiIcons
import org.apache.commons.lang.StringUtils
import org.dartlang.analysis.server.protocol.CompletionSuggestion
import java.util.*
import javax.swing.Icon


class FlutterIconCompletionContributor : DartCompletionExtension() {

	override fun createLookupElement(project: Project, suggestion: CompletionSuggestion): LookupElementBuilder? {
		val icon = findIcon(suggestion) ?: return null

		return DartServerCompletionContributor
				.createLookupElement(project, suggestion)
				.withTypeText("", icon, false)
				.withTypeIconRightAligned(true)
	}

	private fun findIcon(suggestion: CompletionSuggestion): Icon? {
		val element = suggestion.element
		if (element != null) {
			val returnType = element.returnType
			if (!StringUtils.isEmpty(returnType)) {
				element.name?.let { name ->
					return when (suggestion.declaringType) {
						"FontAwesome" -> {
							FontAwesomeIcons.getIcon(name)
						}
						"Ionicons" -> {
							IonIcons.getIcon(name)
						}
						"MaterialCommunityIcons" -> {
							MaterialCommunityIcons.getIcon(name)
						}
						"MdiIcons" -> {
							MdiIcons.getIcon(name)
						}
						else -> null
					}
				}
			}
		}

		return null
	}


}