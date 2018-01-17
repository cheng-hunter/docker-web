package net.sakz.docker;

public class Constants {

    public static final String SESSION_KEY = "DOCKER_WEB_CONSOLE";

    public static final String DEFAULT_DATAFORMAT_STR = "yyyy-MM-dd HH:mm:ss";
    
    public static final String[] EXEC_CMD_ONE = {
			"/bin/sh",
			"-c",
			"TERM=xterm-256color; export TERM; [ -x /bin/bash ] && ([ -x /usr/bin/script ] && /usr/bin/script -q -c '/bin/bash' /dev/null || exec /bin/bash) || exec /bin/sh"};
    
    public static final String[] EXEC_CMD_TWO ={"/bin/bash"};
    
}
