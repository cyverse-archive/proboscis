# proboscus

A Clojure utility to initialize the ElasticSearch index for
[Infosquito](https://github.com/iPlantcollaborativeOpenSource/Infosquito).

## Usage

```
proboscus -h host -p port -i index -s shards -r replicas
proboscus --help
```

## Arguments

All of the arguments to proboscus are optional and have default values that will
be used if no other values is specified.

### -h --host

The host name or IP address of the machine where ElasticSearch is running. If
not specified, this argument defaults to `localhost`.

### -p --port

The port number that ElasticSearch is listening to. If not specified, this
argument defaults to `31338`.

### -i --index

The name of the index to create. If not specified, this argument defaults to
`iplant`.

### -s --shards

The number of shards to use for the index. If not specified, this argument
defaults to `1`. Note that the number of shards should not exceed the number of
nodes in the ElasticSearch cluster. The default ElasticSearch installation
created by the RPM packaged by iPlant provides a single-node installation, so
`1` is the only setting that will work with the default configuration.

### -r --replicas

The number of replicas to use for the index. If not specified, this argument
defaults to `0` meaning that a replica of each shard will not be maintained.
Note that each replica consumes an additional node in the cluster. This means,
for example, that 10 nodes will be required to support an index with 5 shards
and 1 replica.

### -? --help

Displays the a help message and exits.

## Bugs and Limitations

There are no known bugs in this utility. Please report any problems to the Core
Software team.

## License

http://iplantcollaborative.org/sites/default/files/iPLANT-LICENSE.txt
