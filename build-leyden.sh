#!/bin/bash
# Project Leyden optimization script for PropEditorX standalone app

echo "1. Building the project and generating classpath..."
./mvnw clean package -DskipTests
./mvnw dependency:build-classpath -pl :io.github.xenogew.propedit -Dmdep.outputFile=cp.txt

# Get the classpath
# We use the JAR file instead of the classes directory to satisfy AppCDS requirements
APP_JAR=$(ls bundles/io.github.xenogew.propedit/target/io.github.xenogew.propedit-*.jar | grep -v "sources.jar" | head -n 1)
CP="$APP_JAR:$(cat cp.txt)"

echo "2. Running training session to generate class list..."
# --enable-native-access=ALL-UNNAMED is required for SWT's native libraries on Java 25
java --enable-native-access=ALL-UNNAMED -XX:DumpLoadedClassList=classes.lst -cp "$CP" io.github.xenogew.propedit.PropEditorX &
PID=$!
sleep 5
kill $PID

echo "3. Generating optimized AppCDS archive (Leyden style)..."
# -Xshare:dump generates the .jsa file based on the training run class list.
java -Xshare:dump -XX:SharedClassListFile=classes.lst -XX:SharedArchiveFile=properties-editor.jsa -cp "$CP"

echo "4. Cleanup..."
rm classes.lst

echo "Optimization complete!"
echo "Run the app with: java -XX:SharedArchiveFile=properties-editor.jsa -XX:+AOTClassLinking -cp \"$CP\" io.github.xenogew.propedit.PropEditorX"
