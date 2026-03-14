#!/bin/bash
# Project Leyden optimization script for PropEditorX standalone app

echo "1. Building the project..."
./mvnw clean compile

# Get the classpath
CP="target/classes:$(cat cp.txt)"

echo "2. Running training session to generate class list..."
# -XX:DumpLoadedClassList generates a list of all classes loaded during this run.
# We run the app briefly and then close it (manual interaction required in training).
# For automation, we can't easily "close" the SWT window, so we'll just run it with a timeout.
java -XX:DumpLoadedClassList=classes.lst -cp "$CP" io.github.xenogew.propedit.PropEditorX &
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
