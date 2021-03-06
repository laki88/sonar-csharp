/*
 * SonarSource :: .NET :: Shared library
 * Copyright (C) 2014-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
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
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonarsource.dotnet.shared.plugins.protobuf;

import com.google.protobuf.AbstractParser;
import org.junit.Test;
import org.sonarsource.dotnet.protobuf.SonarAnalyzer;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class FileMetadataImporterTest {
  @Test
  public void getGeneratedFilePaths_returns_only_generated_paths() {
    // Arrange
    AbstractParser<SonarAnalyzer.FileMetadataInfo> parser = mock(AbstractParser.class);
    FileMetadataImporter fileMetadataImporter = new FileMetadataImporter(parser);

    SonarAnalyzer.FileMetadataInfo.Builder builder = SonarAnalyzer.FileMetadataInfo.newBuilder();

    SonarAnalyzer.FileMetadataInfo message1 = builder.setIsGenerated(true).setFilePath("file1").build();
    SonarAnalyzer.FileMetadataInfo message2 = builder.setIsGenerated(true).setFilePath("file2").build();
    SonarAnalyzer.FileMetadataInfo message3 = builder.setIsGenerated(false).setFilePath("file3").build();
    SonarAnalyzer.FileMetadataInfo messageSamePath = builder.setIsGenerated(false).setFilePath("file3").build();

    fileMetadataImporter.consume(message1);
    fileMetadataImporter.consume(message2);
    fileMetadataImporter.consume(message3);
    fileMetadataImporter.consume(messageSamePath);

    // Act
    Set<String> paths = fileMetadataImporter.getGeneratedFilePaths();

    // Assert
    assertThat(paths.size()).isEqualTo(2);
    assertThat(paths.contains("file1")).isTrue();
    assertThat(paths.contains("file2")).isTrue();
    assertThat(paths.contains("file3")).isFalse();
  }

  @Test
  public void getGeneratedFilePaths_returns_empty_set_when_protobuf_is_empty() {
    // Arrange
    AbstractParser<SonarAnalyzer.FileMetadataInfo> parser = mock(AbstractParser.class);
    FileMetadataImporter fileMetadataImporter = new FileMetadataImporter(parser);

    // No consume calls means that the protobuf contained no messages

    // Act
    Set<String> paths = fileMetadataImporter.getGeneratedFilePaths();

    // Assert
    assertThat(paths.isEmpty()).isTrue();
  }
}
