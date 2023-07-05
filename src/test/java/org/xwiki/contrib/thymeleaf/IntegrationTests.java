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
package org.xwiki.contrib.thymeleaf;

import javax.inject.Provider;
import javax.script.ScriptContext;
import javax.script.SimpleScriptContext;

import org.xwiki.model.reference.DocumentReference;
import org.xwiki.observation.ObservationManager;
import org.xwiki.refactoring.internal.ModelBridge;
import org.xwiki.rendering.test.integration.junit5.RenderingTests;
import org.xwiki.script.ScriptContextManager;
import org.xwiki.security.authorization.ContextualAuthorizationManager;
import org.xwiki.skinx.SkinExtension;
import org.xwiki.template.TemplateManager;
import org.xwiki.test.TestEnvironment;
import org.xwiki.test.annotation.AllComponents;
import org.xwiki.test.annotation.ComponentList;
import org.xwiki.test.mockito.MockitoComponentManager;

import com.xpn.xwiki.XWiki;
import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.doc.XWikiDocument;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * {@link ThymeleafManager} integration tests.
 *
 * @version $Id$
 * @since 1.0
 */
@AllComponents
@ComponentList(TestEnvironment.class)
public class IntegrationTests implements RenderingTests
{
    @Initialized
    public void initialize(MockitoComponentManager componentManager) throws Exception
    {
        componentManager.registerComponent(TestEnvironment.class);
        componentManager.registerMockComponent(TemplateManager.class);
        componentManager.registerMockComponent(SkinExtension.class, "ssx");
        componentManager.registerMockComponent(ContextualAuthorizationManager.class);
        componentManager.registerMockComponent(ModelBridge.class);
        componentManager.registerMockComponent(ObservationManager.class);

        Provider<XWikiContext> xwikiContextProvider =
            componentManager.registerMockComponent(XWikiContext.TYPE_PROVIDER);
        XWikiContext xcontext = mock(XWikiContext.class);
        when(xwikiContextProvider.get()).thenReturn(xcontext);

        XWiki xWiki = mock(XWiki.class);
        when(xcontext.getWiki()).thenReturn(xWiki);
        when(xWiki.getDocument(any(DocumentReference.class), any())).thenReturn(mock(XWikiDocument.class));

        ScriptContextManager scm = componentManager.registerMockComponent(ScriptContextManager.class);
        SimpleScriptContext scriptContext = new SimpleScriptContext();
        scriptContext.setAttribute("var", "</span><script>console.log('p0wned');</script>", ScriptContext.ENGINE_SCOPE);
        when(scm.getScriptContext()).thenReturn(scriptContext);
    }
}

