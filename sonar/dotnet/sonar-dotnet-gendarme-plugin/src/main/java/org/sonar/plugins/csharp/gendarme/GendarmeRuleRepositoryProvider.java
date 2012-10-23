/*
 * Sonar .NET Plugin :: Gendarme
 * Copyright (C) 2010 Jose Chillan, Alexandre Victoor and SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.csharp.gendarme;

import org.sonar.api.ExtensionProvider;
import org.sonar.api.ServerExtension;
import org.sonar.api.platform.ServerFileSystem;
import org.sonar.api.rules.XMLRuleParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates FXCop rule repositories for every language supported by FxCop.
 */
public class GendarmeRuleRepositoryProvider extends ExtensionProvider implements ServerExtension {

  private ServerFileSystem fileSystem;
  private XMLRuleParser xmlRuleParser;

  public GendarmeRuleRepositoryProvider(ServerFileSystem fileSystem) {
    this.fileSystem = fileSystem;
    this.xmlRuleParser = new XMLRuleParser();
  }

  @Override
  public Object provide() {
    List<GendarmeRuleRepository> extensions = new ArrayList<GendarmeRuleRepository>();

    for (String languageKey : GendarmeConstants.SUPPORTED_LANGUAGES) {
      String repoKey = GendarmeConstants.REPOSITORY_KEY;
      if (!"cs".equals(languageKey)) {
        // every repository key should be "gendarme-<language_key>", except for C# for which it is simply "fxcop" (for backward
        // compatibility)
        repoKey += "-" + languageKey;
      }
      extensions.add(new GendarmeRuleRepository(repoKey, languageKey, fileSystem, xmlRuleParser));
    }

    return extensions;
  }

}
