package hudson.plugins.svn_release_mgr.model;


import hudson.model.BuildBadgeAction;
import hudson.model.Hudson;
import hudson.model.Run;
import hudson.scm.SubversionTagAction;

import java.util.HashMap;
import java.util.Map;

import org.tmatesoft.svn.core.SVNLogEntry;

public class Revision {
	private Map<Long,Run> builds;
	private SVNLogEntry logEntry;
	private static final String SVN_REVISION_KEY = "SVN_REVISION";
	private static final String BUILD_NUMBER_KEY = "BUILD_NUMBER";
	
	public Revision(SVNLogEntry logEntry) {
		this.builds = new HashMap<Long, Run>();
		this.logEntry = logEntry;
	}
	public SVNLogEntry getLogEntry() {
		return logEntry;
	}
	public void setLogEntry(SVNLogEntry logEntry) {
		this.logEntry = logEntry;
	}
	public Map<Long, Run> getBuilds() {
		return builds;
	}
	
	public void addBuild(Run r) {
		builds.put(getBuildNumber(r), r);
	}
	
	public boolean hasTagAction(Long buildNum) {
		boolean hasTagAction = false;
		for (Object o : builds.get(buildNum).getBadgeActions()) {
			BuildBadgeAction ba = (BuildBadgeAction)o;
			if (ba instanceof SubversionTagAction) hasTagAction = true;
		}
		return hasTagAction;
	}
	
	public static Long getBuildNumber(Run r) {
		return Long.parseLong((String)r.getEnvVars().get(BUILD_NUMBER_KEY));
		
	}
	
	public static String getBuildUrl(Run r) {
		return Hudson.getInstance().getRootUrl() + r.getUrl();
	}
	
	public static Long getRevisionNumber(Run r) {
		Long rev = null;
		String revString = (String)r.getEnvVars().get(SVN_REVISION_KEY);
		if (revString != null) rev = Long.parseLong(revString);
		return rev; 
	}
	
}
