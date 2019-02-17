JFreeChart - Future State Edition (FSE)
=======================================

This is a development version of JFreeChart that has been branched from the
JFreeChart 1.0.x series.  Here are some important notes:

-  the API has been changed and will continue to change until I (and the other
   contributors) feel it is ready to be frozen - that may be in 6 months, or
   it may be in 6 years;

-  this code is going to require AT LEAST JDK 1.6.0 to compile and, depending 
   on how I (and the other contributors) feel, in the future we may even 
   require JDK 1.7 or even a later version;

-  Maven is used to build this edition of JFreeChart;

-  JCommon is no longer a dependency (the subset of classes from JCommon that
   are still used have been incorporated directly in the source tree);

The aim is to modernise the JFreeChart API and code and have some fun with new 
stuff and NOT to be constrained by backwards compatibility.

David Gilbert (david.gilbert@object-refinery.com)  
JFreeChart Project Leader

[![Build Status](https://buildhive.cloudbees.com/job/jfree/job/jfreechart-fse/badge/icon)](https://buildhive.cloudbees.com/job/jfree/job/jfreechart-fse/)

