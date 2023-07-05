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

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.script.ScriptContext;

import org.apache.commons.io.IOUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xwiki.component.annotation.Component;
import org.xwiki.contrib.thymeleaf.ThymeleafManager;
import org.xwiki.script.ScriptContextManager;

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

    @Override
    public void evaluate(Writer writer, Reader reader)
    {
        TemplateEngine templateEngine = new TemplateEngine();
        Context context = new Context();

        ScriptContext scriptContext = scriptContextManager.getScriptContext();

        copyScriptContext(scriptContext, context, GLOBAL_SCOPE);
        copyScriptContext(scriptContext, context, ENGINE_SCOPE);

        try {
            String string = IOUtils.toString(reader);
            writer.write(templateEngine.process(string, context));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void copyScriptContext(ScriptContext scriptContext, Context context, int scope)
    {
        context.setVariables(scriptContext.getBindings(scope));
    }
}
