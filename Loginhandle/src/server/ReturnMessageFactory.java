package server;

import util.DatabaseReturnMessage;

public class ReturnMessageFactory {

	public String buildMessage(DatabaseReturnMessage drm) {
		StringBuilder sb = new StringBuilder();
		sb.append(drm.permission+ ":" +drm.errorMessage+ "\n");
		return sb.toString();
	}
}
