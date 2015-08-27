set classpath="../../src:.:../../lib/guava-18.0.jar";

echo `javac -classpath ../../src:.:../../lib/guava-18.0.jar ../../src/*/*.java TestTrain.java`;
echo "javac -classpath ../../src:.:../../lib/guava-18.0.jar ../../src/*/*.java TestTrain.java";

echo "java -server -Xmx1500m -classpath ../../src:.:../../lib/guava-18.0.jar PercyToTom";
echo `java -server -Xmx1500m -classpath ../../src:.:../../lib/guava-18.0.jar generateTest/PercyToTom`;


