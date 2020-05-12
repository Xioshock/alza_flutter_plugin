package cz.alza.flutter.templates.livetemplates

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider

class DartTemplateProvider : DefaultLiveTemplatesProvider {

	override fun getDefaultLiveTemplateFiles(): Array<String> {
		return arrayOf("liveTemplates/Dart")
	}

	override fun getHiddenLiveTemplateFiles(): Array<String>? {
		return arrayOf()
	}
}
