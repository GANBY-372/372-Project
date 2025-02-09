# Building the Project

This project was built with IntelliJ. Building the project under a different 
IDE might fail. IntelliJ's build process, library locations might differ from
other build systems.

## Java Version
Oracle Open JDK 23. If you want to use a different version adjust your build 
configuration.

## Building with Maven
If the driver classes cannot run, Maven might need to build the project.
You can build from the command line or the Maven View.

## 1. Getting to the Maven View
View ->Tool Windows -> Maven
This will open Maven on the Right Tool Pane. The Maven icon is an italicized M
that says Maven below.

## 2. Cleaning, Validating, and Installing the Project
Dragging the Maven Panel to the left shows its' options.
Click on the caret (>) next to the M gamby for the project
Expand the menu by clicking >(M ganby) -> Lifecycle

i. Run clean
ii. After cleaning succeeds run validate
iii. Compile
iv. Install

The build process can fail if there are syntax errors in your code.
They will need to be fixed. Maven's messages are more descriptive
than Gradle's.

### 3. Run the default Main driver class.
The project comes with a Main class that confirms the project was 
built successfully. If this does not display "Hello World" and a loop 
there are problems that need to be fixed. Fix them.

If the default test fails our code is likely to fail in complex ways.
See the screenshot maven-build-results.png