package org.mule.tooling.esb.config.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.mule.tooling.esb.config.MuleElementDefinition;
import org.mule.tooling.esb.config.MuleElementDefinitionService;
import org.mule.tooling.esb.config.MuleModuleDefinition;

import java.util.List;

public class MuleElementCompletionProvider extends CompletionProvider<CompletionParameters> {
    @Override
    protected void addCompletions(@NotNull CompletionParameters completionParameters, ProcessingContext processingContext, @NotNull CompletionResultSet completionResultSet) {
        final Project project = completionParameters.getOriginalFile().getProject();
        final MuleElementDefinitionService instance = MuleElementDefinitionService.getInstance(project);
        final List<MuleModuleDefinition> definitions = instance.getDefinitions();
        for (MuleModuleDefinition definition : definitions) {
            final List<MuleElementDefinition> elementDefinitions = definition.getElementDefinitions();
            for (MuleElementDefinition elementDefinition : elementDefinitions) {
                final LookupElementBuilder lookupElement =
                        LookupElementBuilder.create(elementDefinition.getName())
                                .withCaseSensitivity(false)
                                .withLookupString(definition.getName() + ":" + elementDefinition.getName())
                                .withTypeText("\t" + StringUtil.capitalizeWords(elementDefinition.getType().name().toLowerCase(), "_", true, false), true)
                                .withPresentableText(definition.getName() + ":" + elementDefinition.getName())
                                .withInsertHandler(new MuleElementInsertHandler(elementDefinition.getName(), definition.getName(), definition.getNamespace(), definition.getLocationLookup()));
                completionResultSet.addElement(lookupElement);
            }
        }
        completionResultSet.stopHere();
    }
}
