/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011-2012, Sebastian Staudt
 */

package com.github.koraktor.mavanagaiata;

import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class GitChangelogMojoTest extends AbstractGitOutputMojoTest<GitChangelogMojo> {

    @Test
    public void testError() {
        super.testError("Unable to generate changelog from Git");
    }

    @Test
    public void testCreateGitHubLinks() throws Exception {
        this.mojo.init();
        assertThat(this.mojo.createGitHubLinks, is(false));

        this.mojo.gitHubProject = "";
        this.mojo.init();
        assertThat(this.mojo.createGitHubLinks, is(false));

        this.mojo.gitHubProject = "mavanagaiata";
        this.mojo.init();
        assertThat(this.mojo.createGitHubLinks, is(false));

        this.mojo.gitHubUser = "";
        this.mojo.init();
        assertThat(this.mojo.createGitHubLinks, is(false));

        this.mojo.gitHubUser = "koraktor";
        this.mojo.init();
        assertThat(this.mojo.createGitHubLinks, is(false));

        this.mojo.createGitHubLinks = true;
        this.mojo.init();
        assertThat(this.mojo.createGitHubLinks, is(true));
    }

    @Test
    public void testCustomization() throws Exception {
        this.mojo.baseDateFormat    = "MM/dd/yy";
        this.mojo.commitPrefix      = "- ";
        this.mojo.createGitHubLinks = true;
        this.mojo.dateFormat        = "dd.MM.yyyy";
        this.mojo.footer            = "\nCreated by Mavanagaiata on %s";
        this.mojo.gitHubProject     = "mavanagaiata";
        this.mojo.gitHubUser        = "koraktor";
        this.mojo.header            = "History\\n-------\\n";
        this.mojo.tagPrefix         = "\nTag ";
        this.mojo.execute();

        assertThat(this.reader.readLine(), is(equalTo("History")));
        assertThat(this.reader.readLine(), is(equalTo("-------")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo("Commits on branch \"master\"")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo("- Snapshot for version 3.0.0")));
        assertThat(this.reader.readLine(), is(equalTo("- Added project name")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo("See Git history for changes in the \"master\" branch since version 2.0.0 at: https://github.com/koraktor/mavanagaiata/compare/2.0.0...master")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo("Tag 2.0.0 - 03.05.2011")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo("- Version bump to 2.0.0")));
        assertThat(this.reader.readLine(), is(equalTo("- Snapshot for version 2.0.0")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo("See Git history for version 2.0.0 at: https://github.com/koraktor/mavanagaiata/compare/1.0.0...2.0.0")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo("Tag 1.0.0 - 03.05.2011")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo("- Initial commit")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo("See Git history for version 1.0.0 at: https://github.com/koraktor/mavanagaiata/commits/1.0.0")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertMatches("Created by Mavanagaiata on \\d{2}/\\d{2}/\\d{2}", reader.readLine());
        assertThat(this.reader.ready(), is(false));
    }

    @Test
    public void testStartTagged() throws IOException, MojoExecutionException {
        this.mojo.head = "HEAD^^";
        this.mojo.execute();

        assertThat(this.reader.readLine(), is(equalTo("Changelog")));
        assertThat(this.reader.readLine(), is(equalTo("=========")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo("Version 2.0.0 - 05/03/2011 07:18 AM +0200")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo(" * Version bump to 2.0.0")));
        assertThat(this.reader.readLine(), is(equalTo(" * Snapshot for version 2.0.0")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo("Version 1.0.0 - 05/03/2011 07:18 AM +0200")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo(" * Initial commit")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertMatches("Generated by Mavanagaiata at \\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2} [AP]M [+\\-]\\d{4}", this.reader.readLine());
        assertThat(this.reader.ready(), is(false));
    }

    @Test
    public void testSkipTagged() throws Exception {
        this.mojo.footer     = "";
        this.mojo.skipTagged = true;
        this.mojo.execute();

        assertThat(this.reader.readLine(), is(equalTo("Changelog")));
        assertThat(this.reader.readLine(), is(equalTo("=========")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo("Commits on branch \"master\"")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo(" * Snapshot for version 3.0.0")));
        assertThat(this.reader.readLine(), is(equalTo(" * Added project name")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo("Version 2.0.0 - 05/03/2011 07:18 AM +0200")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo(" * Snapshot for version 2.0.0")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo("Version 1.0.0 - 05/03/2011 07:18 AM +0200")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.ready(), is(false));
    }

    @Test
    public void testUntaggedProject() throws Exception {
        this.mojo.baseDir           = this.getRepository("untagged-project");
        this.mojo.createGitHubLinks = true;
        this.mojo.gitHubProject     = "mavanagaiata";
        this.mojo.gitHubUser        = "koraktor";
        this.mojo.footer            = "";
        this.mojo.skipTagged        = true;
        this.mojo.execute();

        assertThat(this.reader.readLine(), is(equalTo("Changelog")));
        assertThat(this.reader.readLine(), is(equalTo("=========")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo("Commits on branch \"master\"")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo(" * Snapshot for version 3.0.0")));
        assertThat(this.reader.readLine(), is(equalTo(" * Version bump to 2.0.0")));
        assertThat(this.reader.readLine(), is(equalTo(" * Snapshot for version 2.0.0")));
        assertThat(this.reader.readLine(), is(equalTo(" * Initial commit")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo("See Git history for changes in the \"master\" branch at: https://github.com/koraktor/mavanagaiata/commits/master")));
        assertThat(this.reader.ready(), is(false));
    }

    protected void assertOutput() throws IOException {
        assertThat(this.reader.readLine(), is(equalTo("Changelog")));
        assertThat(this.reader.readLine(), is(equalTo("=========")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo("Commits on branch \"master\"")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo(" * Snapshot for version 3.0.0")));
        assertThat(this.reader.readLine(), is(equalTo(" * Added project name")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo("Version 2.0.0 - 05/03/2011 07:18 AM +0200")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo(" * Version bump to 2.0.0")));
        assertThat(this.reader.readLine(), is(equalTo(" * Snapshot for version 2.0.0")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo("Version 1.0.0 - 05/03/2011 07:18 AM +0200")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertThat(this.reader.readLine(), is(equalTo(" * Initial commit")));
        assertThat(this.reader.readLine(), is(equalTo("")));
        assertMatches("Generated by Mavanagaiata at \\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2} [AP]M [+\\-]\\d{4}", this.reader.readLine());
        assertThat(this.reader.ready(), is(false));
    }

}
