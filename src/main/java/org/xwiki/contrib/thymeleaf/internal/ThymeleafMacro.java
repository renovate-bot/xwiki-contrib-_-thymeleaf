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

import java.io.StringReader;
import java.io.StringWriter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.contrib.thymeleaf.ThymeleafManager;
import org.xwiki.rendering.macro.MacroExecutionException;
import org.xwiki.rendering.macro.descriptor.DefaultContentDescriptor;
import org.xwiki.rendering.macro.script.AbstractScriptMacro;
import org.xwiki.rendering.macro.script.ScriptMacroParameters;
import org.xwiki.rendering.transformation.MacroTransformationContext;

/**
 * Thymeleaf Macro.
 *
 * @version $Id$
 * @since 1.0
 */
@Component
@Named("thymeleaf")
@Singleton
public class ThymeleafMacro extends AbstractScriptMacro<ScriptMacroParameters>
{
    @Inject
    protected ThymeleafManager thymeleafManager;

    /**
     * Default constructor.
     */
    public ThymeleafMacro()
    {
        super("Thymeleaf", "Executes a Thymeleaf script.",
            new DefaultContentDescriptor("the thymeleaf script to execute"),
            ScriptMacroParameters.class);
    }

    @Override
    public boolean supportsInlineMode()
    {
        return true;
    }

    @Override
    protected String evaluateString(ScriptMacroParameters parameters, String content,
        MacroTransformationContext context) throws MacroExecutionException
    {
        String key = context.getTransformationContext().getId();
        if (key == null) {
            throw new MacroExecutionException("unknown namespace");
        }

        StringWriter stringWriter = new StringWriter();
        this.thymeleafManager.evaluate(stringWriter, new StringReader(content));

        return stringWriter.toString();
    }
}
