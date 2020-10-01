package com.corp.concepts.raci.model;

public class Messages {

	public class Error {
		public static final String UND_STAKEHOLDER = "Undefined stakeholder: [%s]!";
		public static final String EMPTY_STAKEHOLDER = "No stakeholder exists!";

		public static final String UND_RESPONSIBILITY = "Undefined responsibility: [%s]!";

		public static final String NO_NAME_SHOLDER = "No name provided for stakeholder!";

		public static final String EMPTY_ASSIGNMENT = "No assignment exists!";

		public static final String TASK_EXISTS = "This task already exists!";
		
		public static final String WRONG_OLD_PASSWORD = "Wrong old password!";
	}

	public class Success {
		public static final String TASK_ADDED = "Task added";
		public static final String TASK_DELETED = "Task deleted: %d";
		public static final String USER_ADDED = "User added: %s";
		public static final String PASSWORD_CHANGED = "Password changed";
	}

}
