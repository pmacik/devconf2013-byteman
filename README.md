devconf2013-byteman
===================

The sources for Byteman lab on DevConf 2013

To run the server:
$ mvn clean compile exec:java -Pserver

To run the bugged client:
$ mvn clean compile exec:java -Pclient

To run the bugged client with Byteman attached:
$ MAVEN_OPTS="-javaagent:$BYTEMAN_HOME/lib/byteman.jar=script:$(pwd)/src/main/resources/ClientMain.btm" mvn clean compile exec:java -Pclient

To run the fixed client:
$ mvn clean compile exec:java -Pfixed-client