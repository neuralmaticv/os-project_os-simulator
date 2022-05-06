package com.college.os_project.model.kernel;

public enum ProcessState {
	READY, RUNNING, BLOCKED, TERMINATED, DONE;

	public String getState() {
		return this.name();
	}
}
