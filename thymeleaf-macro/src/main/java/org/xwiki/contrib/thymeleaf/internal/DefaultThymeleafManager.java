/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.contrib.thymeleaf.internal;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.script.ScriptContext;

import org.apache.commons.io.IOUtils;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.cache.AlwaysValidCacheEntryValidity;
import org.thymeleaf.cache.ICacheEntryValidity;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.AbstractTemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.templateresource.StringTemplateResource;
import org.xwiki.component.annotation.Component;
import org.xwiki.contrib.thymeleaf.ThymeleafManager;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.DocumentReferenceResolver;
import org.xwiki.script.ScriptContextManager;

import com.xpn.xwiki.XWiki;
import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.doc.XWikiDocument;

import static javax.script.ScriptContext.ENGINE_SCOPE;
import static javax.script.ScriptContext.GLOBAL_SCOPE;

/**
 * Default implementation of {@link ThymeleafManager}, copy the script context into the velocity context and renders the
 * provided script.
 *
 * @version $Id$
 * @since 1.0
 */
@Component
@Singleton
public class DefaultThymeleafManager implements ThymeleafManager
{
    @Inject
    private ScriptContextManager scriptContextManager;

    @Inject
    @Named("current")
    private DocumentReferenceResolver<String> referenceResolver;

    @Inject
    private Provider<XWikiContext> contextProvider;

    @Override
    public void evaluate(Writer writer, Reader reader)
    {
        TemplateEngine templateEngine = new TemplateEngine();
        XWikiTemplateResolver xWikiTemplateResolver = new XWikiTemplateResolver();
//        xWikiTemplateResolver.setCheckExistence(true);
        xWikiTemplateResolver.setOrder(1);
        StringTemplateResolver stringTemplateResolver = new StringTemplateResolver();
        stringTemplateResolver.setOrder(2);
        templateEngine.setTemplateResolvers(Set.of(
            stringTemplateResolver,
            xWikiTemplateResolver
        ));
        Context context = new Context();

        ScriptContext scriptContext = scriptContextManager.getScriptContext();

        copyScriptContext(scriptContext, context, GLOBAL_SCOPE);
        copyScriptContext(scriptContext, context, ENGINE_SCOPE);

        try {
            String string = IOUtils.toString(reader);
            templateEngine.process(string, context, writer);
        } catch (IOException e) {
            // TODO: improve!
            throw new RuntimeException(e);
        }
    }

    private void copyScriptContext(ScriptContext scriptContext, Context context, int scope)
    {
        context.setVariables(scriptContext.getBindings(scope));
    }

    private class XWikiTemplateResolver extends AbstractTemplateResolver
    {
        @Override
        protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate,
            String template, Map<String, Object> templateResolutionAttributes)
        {
            DocumentReference resolve = referenceResolver.resolve(template);
            try {
                XWikiContext xcontext = contextProvider.get();
                XWiki wiki = xcontext.getWiki();
                XWikiDocument document = wiki.getDocument(resolve, xcontext);
                if (!document.isNew()) {
                    return new StringTemplateResource(document.getRenderedContent(xcontext));
                }
            } catch (XWikiException e) {
                return null;
            }
            return null;
        }

        @Override
        protected TemplateMode computeTemplateMode(IEngineConfiguration configuration, String ownerTemplate,
            String template, Map<String, Object> templateResolutionAttributes)
        {
            return TemplateMode.HTML;
        }

        @Override
        protected ICacheEntryValidity computeValidity(IEngineConfiguration configuration, String ownerTemplate,
            String template, Map<String, Object> templateResolutionAttributes)
        {
            return AlwaysValidCacheEntryValidity.INSTANCE;
        }
    }
}
