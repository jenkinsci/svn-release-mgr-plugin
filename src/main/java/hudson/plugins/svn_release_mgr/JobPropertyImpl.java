package hudson.plugins.svn_release_mgr;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.scm.SubversionReleaseSCM;

import java.util.logging.Logger;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Job property for svn-release-mgr.
 */
public final class JobPropertyImpl extends JobProperty<AbstractProject<?,?>> {

	private static final Logger LOGGER = Logger.getLogger(JobPropertyImpl.class.getName());
	public final String maxRevisions;
	
	@DataBoundConstructor
	public JobPropertyImpl(String maxRevisions) {
		this.maxRevisions = maxRevisions;
		
	}
	
	@Override
	public Action getJobAction(AbstractProject<?, ?> job) {
		Action action = super.getJobAction(job);
                // I had to use a copy of SubversionSCM, but could have used
                // SubversionSCM if we can have 3 lines added to CheckOutTask.
		if (SubversionReleaseSCM.class.equals( job.getScm().getClass())) {
			action = new ProjectReleaseAction(job, this);
		}

		return action;
	}


    /**
     * Descriptor for Subversion Release Manager job property.
     */
    @Extension
    public static final class DescriptorImpl extends JobPropertyDescriptor {

        public DescriptorImpl() {
            super(JobPropertyImpl.class);
            load();
        }

		@Override
		public String getDisplayName() {
			return "Subversion Releases";
		}

		@Override
		public JobProperty<?> newInstance(StaplerRequest req, JSONObject formData) throws FormException {
			if (req.getParameter("releases.show") != null) 
				return new JobPropertyImpl(req.getParameter("maxRevisions"));
			else
				return null;
		}

    }
}
