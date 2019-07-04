# sstable-level-reset
Resets level of SSTables when using LCS in Apache Cassandra. 

**Use on your own risk.**

Cassandra's _sstablelevelreset_ [tool](https://github.com/apache/cassandra/blob/trunk/tools/bin/sstablelevelreset) is very blunt because it allows resetting levels of **all** SSTables for given table. This tool tries to be a bit more precise and reset levels of specific files.

Works with Cassandra 2.2.9.

## Build

Simply by:

```bash
mvn package
```

## Usage

There are two modes of operation. First one lists which level a table is on:
```
java -jar sstablelevelreset-0.0.1-SNAPSHOT.jar -o list -f ~/.ccm/2-2-9/node1/data0/tlp_stress/sensor_data-e14719e0935411e9b1cd6d8bc4dd4750/lb-613-big-Data.db
2 ~/.ccm/2-2-9/node1/data0/tlp_stress/sensor_data-e14719e0935411e9b1cd6d8bc4dd4750/lb-613-big-Data.db
```

The second actually resets the level:
```
java -jar sstablelevelreset-0.0.1-SNAPSHOT.jar -o reset -f ~/.ccm/2-2-9/node1/data0/tlp_stress/sensor_data-e14719e0935411e9b1cd6d8bc4dd4750/lb-613-big-Data.db
2 ~/.ccm/2-2-9/node1/data0/tlp_stress/sensor_data-e14719e0935411e9b1cd6d8bc4dd4750/lb-613-big-Data.db
```
