package de.uni_passau.fim.infosun.prophet.log;


public aspect LogAspect {
//
//	static HashMap<String, Logger> logger = new HashMap<String, Logger>();
//	static Logger bigLog = Logger.getLogger("bigLog");
//
//	static {
//		try {
//			File logDir = new File("log");
//			if(!logDir.isDirectory()) {
//				logDir.mkdir();
//			}
//			Handler h = new FileHandler("log" + System.getProperty("file.separator") + "bigLog.txt");
//			h.setFormatter(new SimpleFormatter());
//			bigLog.addHandler(h);
//			bigLog.setLevel(Level.CONFIG);
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private void addLogger(String className) {
//		try {
//			Logger log = Logger.getLogger(className);
//			Handler fileHandler = new FileHandler("log" + System.getProperty("file.separator") + className
//					+ ".txt");
//			fileHandler.setFormatter(new SimpleFormatter());
//			log.addHandler(fileHandler);
//			log.setLevel(Level.CONFIG);
//			logger.put(className, log);
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private String createArgs(JoinPoint thisJoinPoint) {
//		StringBuffer args = new StringBuffer();
//		if (thisJoinPoint.getArgs().length!=0) {
//			args.append("(");
//			for (int i = 0; i < thisJoinPoint.getArgs().length; i++) {
//				if (i > 0)
//					args.append(",");
//				if(thisJoinPoint.getArgs()[i] == null) {
//					args.append("null");
//				} else {
//					args.append(thisJoinPoint.getArgs()[i].toString());
//				}
//			}
//			args.append(")");
//		}
//		return args.toString();
//	}
//
//	before(): execution(* *..*(..)) && !within(LogAspect){
//		String className = thisJoinPoint.getSourceLocation().getFileName();
//		if (!logger.containsKey(className)) {
//			addLogger(className);
//		}
//		logger.get(className).config(
//				"--> " + thisJoinPoint.getSignature().toString() + createArgs(thisJoinPoint) + " at "
//						+ thisJoinPoint.getSourceLocation());
//		bigLog.config("--> " + thisJoinPoint.getSignature().toString() + createArgs(thisJoinPoint) + " at "
//				+ thisJoinPoint.getSourceLocation());
//	}
//
//	after() returning(Object o): execution(* *..*(..)) && !within(LogAspect){
//		String className = thisJoinPoint.getSourceLocation().getFileName();
//		if (!logger.containsKey(className)) {
//			addLogger(className);
//		}
//		logger.get(className).config(
//				"<-- " + thisJoinPoint.getSignature().toString() + " at " + thisJoinPoint.getSourceLocation()
//						+ " returning: " + o);
//		bigLog.config("<-- " + thisJoinPoint.getSignature().toString() + " at "
//				+ thisJoinPoint.getSourceLocation() + " returning: " + o);
//	}
//
//	before(): execution(new(..)) && !within(LogAspect){
//		String className = thisJoinPoint.getSourceLocation().getFileName();
//		if (!logger.containsKey(className)) {
//			addLogger(className);
//		}
//		logger.get(className).config(
//				"CONSTRUCT " + thisJoinPoint.getSignature().toString() + createArgs(thisJoinPoint) + " at "
//						+ thisJoinPoint.getSourceLocation());
//		bigLog.config("CONSTRUCT " + thisJoinPoint.getSignature().toString() + createArgs(thisJoinPoint) + " at "
//				+ thisJoinPoint.getSourceLocation());
//	}
//
//	after() throwing (Exception exception) : call (* *..*(..)) {
//		String className = thisJoinPoint.getSourceLocation().getFileName();
//		if (!logger.containsKey(className)) {
//			addLogger(className);
//		}
//		logger.get(className).config(
//				"ERROR: Inside the class " + thisJoinPoint.getSourceLocation().getFileName() + " at line "
//						+ thisJoinPoint.getSourceLocation().getLine() + " a call was made to "
//						+ thisJoinPoint.getSignature().toShortString() + " and a Exception was thrown.");
//		logger.get(className).config("\t" + exception.toString());
//		bigLog.config("ERROR: Inside the class " + thisJoinPoint.getSourceLocation().getFileName() + " at line "
//				+ thisJoinPoint.getSourceLocation().getLine() + " a call was made to "
//				+ thisJoinPoint.getSignature().toShortString() + " and a Exception was thrown.");
//		bigLog.config("\t" + exception.toString());
//	}
}
