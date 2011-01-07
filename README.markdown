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

Usage: stagekillaz [-h] -OPT1 VALUE1 --LONGOPT2=VALUE2 ... --OPTIONn=VALUEn

Options:
    -h | --help           This short help message
    -i | --inputfile      DataStage export file
    -r | --reposurl       ODI repository's jdbc URL
    -d | --reposdriver    JDBC Driver class
    -s | --reposuser      Database user for master repo
    -a | --repospassword  Database password for master repo
    -n | --reposname      Working repository name (optional)
    -u | --odiuser        ODI user name
    -p | --odipassword    ODI password
    -f | --folder         Folder in ODI project (optional)
    -k | --kmpath         Path to ODI's xml-reference directory (optional)

Credits
-------

All rights reserved, Tamas Foldi, Starschema Limited [www.starschema.net](http://starschema.net/).
Licensed under BSD License.