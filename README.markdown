Stagekillaz
===========

Description
-----------

Stagekillaz is an IBM DataStage-to-Oracle Data Integrator (ODI) converter, which takes a DataStage export file
(DSE XML) and uploads it to an ODI metadata repository. 

The following conversions could be avaialble:

* Import DS variables as ODI variables
* Create ODI topology based on DS topology 
* Migrate stages into interfaces
* Create packages based on interfaces

Usage
-----

Usage: stagekillaz [-h] --OPTION1=VALUE1 ... --OPTIONn=VALUEn

Options:
        help            This short help message
        inputfile       DataStage export file
        reposurl        ODI repository's jdbc URL
        reposdriver     JDBC Driver class
        reposuser       Database user for master repo
        repospassword   Database password for master repo
        reposname       Working repository name (optional)
        odiuser         ODI user name
        odipassword     ODI password

Credits
-------

All rights reserved, Tamas Foldi, Starschema Limited [www.starschema.net](http://starschema.net/).
Licensed under BSD License.