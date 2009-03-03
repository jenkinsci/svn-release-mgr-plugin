package hudson.plugins.svn_release_mgr;

import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.scm.SubversionReleaseSCM;

import java.util.logging.Logger;

import org.kohsuke.stapler.DataBoundConstructor;

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

	private boolean show;
	private static final Logger LOGGER = Logger.getLogger(JobPropertyImpl.class.getName());
	
	@DataBoundConstructor
	public JobPropertyImpl(boolean show) {
		this.show = show;
	}
	@Override
	public Action getJobAction(AbstractProject<?, ?> job) {
		Action action = super.getJobAction(job);
		if (SubversionReleaseSCM.class.equals( job.getScm().getClass()) && show) {
			action = new ProjectReleaseAction(job, this);
		}

		return action;
	}

    @Override
	public JobPropertyDescriptor getDescriptor() {
		// TODO Auto-generated method stub
		return DESCRIPTOR;
	}

	/**
     * Descriptor should be singleton.
     */
    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();


	public boolean show() {
		return show;
	}
    /**
     * Descriptor for {@link HelloWorldBuilder}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See <tt>views/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    public static final class DescriptorImpl extends JobPropertyDescriptor {

    	DescriptorImpl() {
            super(JobPropertyImpl.class);
        }

		@Override
		public String getDisplayName() {
			// TODO Auto-generated method stub
			return "Subversion Releases";
		}


    }
}
