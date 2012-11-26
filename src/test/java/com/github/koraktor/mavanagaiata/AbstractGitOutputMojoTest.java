/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011-2012, Sebastian Staudt
 */

package com.github.koraktor.mavanagaiata;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public abstract class AbstractGitOutputMojoTest<T extends AbstractGitOutputMojo>
        extends AbstractMojoTest<T> {

    protected BufferedReader reader;

    protected abstract void assertOutput() throws IOException;

    @Before
    @Override
    public void setup() throws Exception {
        super.setup();

        File tempFile = File.createTempFile("output", null);
        this.mojo.setOutputFile(tempFile);
        this.reader = new BufferedReader(new FileReader(tempFile));
    }

    @After
    public void tearDown() throws IOException {
        this.reader.close();
        if(this.mojo.getOutputFile() != null &&
           !this.mojo.getOutputFile().delete()) {
            this.mojo.getOutputFile().deleteOnExit();
        }
    }

    @Test
    public void testNonExistantDirectory() throws Exception {
        this.reader.close();
        if(!this.mojo.getOutputFile().delete()) {
            this.mojo.getOutputFile().deleteOnExit();
        }
        File tempDir  = File.createTempFile("temp", null);
        tempDir.delete();
        tempDir.deleteOnExit();
        File tempFile = new File(tempDir + "/output");
        this.mojo.setOutputFile(tempFile);
        this.mojo.execute();

        this.reader = new BufferedReader(new FileReader(tempFile));

        this.assertOutput();
    }

    @Test
    public void testResult() throws Exception {
        this.mojo.execute();

        this.assertOutput();
    }

    @Test
    public void testSetOutputFile() {
        File file = new File("./test");
        this.mojo.setOutputFile(file);
        assertThat(this.mojo.getOutputFile(), is(equalTo(file)));
    }

    @Test
    public void testStdOut() throws Exception {
        try {
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            PrintStream stream = new PrintStream(oStream);
            System.setOut(stream);

            this.reader.close();
            if(!this.mojo.getOutputFile().delete()) {
                this.mojo.getOutputFile().deleteOnExit();
            }
            this.mojo.setOutputFile(null);
            this.mojo.execute();

            byte[] output = oStream.toByteArray();
            this.reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(output)));

            this.assertOutput();
        } finally {
            System.setOut(null);
        }
    }

}
