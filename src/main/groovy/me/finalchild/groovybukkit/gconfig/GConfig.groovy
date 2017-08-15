/*
 * This file is part of GroovyBukkit, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 Final Child
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.finalchild.groovybukkit.gconfig

import me.finalchild.groovybukkit.gshell.GShell

import java.nio.file.DirectoryStream
import java.nio.file.Files
import java.nio.file.Path

import com.google.common.io.Files as GFiles

/**
 * Loads Groovy coded config files
 */
final class GConfig {

    private GConfig() {}

    /**
     * Loads all Groovy files in a directory
     * @param directory The directory
     * @return A Map of files' Paths and loaded objects
     */
    static <T> Map<Path, T> loadDir(Path directory) {
        Files.createDirectories(directory)
        return Files.newDirectoryStream(directory, new DirectoryStream.Filter<Path>() {
            boolean accept(Path file) throws IOException {
                if (Files.isDirectory(file)) return false
                String extension = GFiles.getFileExtension(file.toAbsolutePath().toString())
                extension == 'groovy' || extension == 'gvy' || extension == 'gy' || extension == 'gvy'
            }
        }).withCloseable {
            it.collectEntries { file -> [file, loadConfig(file)] }
        }
    }

    /**
     * Loads a Groovy file
     * @param files The file
     * @return The loaded object
     */
    static <T> T loadConfig(Path file) {
        (T) file.withReader('UTF-8') { reader ->
            GShell.shell.evaluate(reader, GFiles.getNameWithoutExtension(file.toAbsolutePath().toString()))
        }
    }

}
