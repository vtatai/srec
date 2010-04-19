srec - a Java Swing record, replay, testing tool
========================================================================================================================
About

srec is a Swing record-replay testing tool useful for testing Swing applications. It provides not only the basic testing
 infrastructure but also a Java DSL and a Ruby DSL for testing Swing applications, even integrating with Ruby Cucumber
 for your daily BDD fix. Another cool feature is the ability to run your tests in a distributed fashion using a Hadoop
 (http://hadoop.apache.org/) cluster (using the Hadoop module).

========================================================================================================================
Using

Using srec is quite easy - once you have included the srec jar in your project you have three options:
1. Write the test in Java using directly the class com.github.srec.play.jemmy.JemmyDSL
2. Record your basic test script running the class com.github.srec.ui.SRecForm
3. Write the test in Ruby using the provided Ruby DSL - check the examples module for samples

srec is not a test harness (and is test-harness agnostic), so you should be able to write your tests either in JUnit
 or TestNG without problems.

For more information on using srec please take a look at the examples.

========================================================================================================================
Code

srec is structured in three main modules

1. Core: contains the core Java classes
2. Examples: contains usage examples
3. Hadoop: contains Hadoop infrastructure classes

The backend for srec is provided by Jemmy (https://jemmy.dev.java.net/). Why Jemmy? It is extremely stable, still
 actively maintained, and supports both AWT event queue and Robot playback - it is our experience that even though
 AWT players are generally faster and more stable, they fail in some scenarios, for instance by not firing events
 / document listeners / document filters attached to text fields. So the bottom line is: use the AWT event queue model
 if you can, the Robot if you must.

Much of the recording code was adapted from project Frankenstein (http://frankenstein.openqa.org). However its replay
infrastructure is not as powerful as Jemmy's and the project is no longer actively maintained. Many thanks to them
though for the great work.

For development IntelliJ IDEA was used, but this only should be relevant in case you want to edit the recording GUI as
 it was made with IDEA's GUI Designer.