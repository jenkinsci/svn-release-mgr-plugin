package hudson.plugins.svn_release_mgr;

import hudson.Plugin;
import hudson.model.Jobs;
import hudson.scm.SCMS;
import hudson.scm.SubversionReleaseSCM;

import java.util.logging.Logger;

/**
 * Entry point of a plugin.
 *
 * <p>
 * This is the Plugin class for the SVN Release Manager plugin.  I had to use
 * a coppy of SubversionSCM, but could have used SubversionSCM if we can have 
 * 3 lines added to CheckOutTask.
 * @author Jesse Piascik
 */
public class PluginImpl extends Plugin {
	private static final Logger LOGGER = Logger.getLogger(PluginImpl.class.getName());
	public void start() throws Exception {
        // plugins normally extend Hudson by providing custom implementations
        // of 'extension points'. In this example, we'll add one builder.
    	Jobs.PROPERTIES.add(JobPropertyImpl.DESCRIPTOR);
    	SCMS.SCMS.add(SubversionReleaseSCM.DescriptorImpl.DESCRIPTOR);
   }
}
