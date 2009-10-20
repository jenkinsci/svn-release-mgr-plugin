package hudson.plugins.svn_release_mgr;

import hudson.Extension;
import hudson.Util;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.scm.SubversionReleaseSCM;
import hudson.util.FormValidation;

import java.util.logging.Logger;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Promotion processes defined for a project.
 *
 * <p>
 * TODO: a possible performance problem as every time the owner job is reconfigured,
 * all the promotion processes get reloaded from the disk.
 *
 * @author Kohsuke Kawaguchi
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
     * Descriptor for {@link HelloWorldBuilder}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See <tt>views/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension
    public static final class DescriptorImpl extends JobPropertyDescriptor {

        public DescriptorImpl() {
            super(JobPropertyImpl.class);
            load();
        }

		@Override
		public String getDisplayName() {
			// TODO Auto-generated method stub
			return "Subversion Releases";
		}

		@Override
		public JobProperty<?> newInstance(StaplerRequest req, JSONObject formData) throws FormException {
			if (req.getParameter("releases.show") != null) 
				return new JobPropertyImpl(req.getParameter("maxRevisions"));
			else
				return null;
		}

		public FormValidation doCheckMaxRevisions(@QueryParameter String value) {
			try {
				Integer.parseInt(Util.nullify(value));
				return FormValidation.ok();
			} catch (NumberFormatException e) {
				return FormValidation.error("Enter an integer value only.");
			}
		}

    }
}
