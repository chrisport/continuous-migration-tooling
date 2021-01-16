# Continuous Migration Tooling: Merging Utility

This JVM library supports rewrites of software in a continuous way, based on principles of evolutionary architecture.   
   
The approach is described in the blogpost [Amortizing Software Rewrites: An Evolutionary Approach](https://medium.com/interleap/amortizing-software-rewrites-an-evolutionary-approach-c34d89ceaf4c). 
It allows you to migrate functionalities seemlesly, with **no defects** and in a **continuous way**, meaning that it can be stopped and continued at any time. 
This can be helpful when splitting off functionality from a monolith into a microservice, or moving from one API to another.

The library is work in progress and includes or will include:
 - :white_check_mark: Inspect 2 objects and report discrepancies (field missing in origin, field missing in rewrite, value mismatch)
 - Ignore discrepancies in specified fields
 - Merge the two objects flexibly:
   - by using original as source of truth
   - override values of specified fields with the values from the rewrite


