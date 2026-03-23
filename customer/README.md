# Customer

Simple REST API backend with Quarkus and frontend with Astro and Vue.

## L1 and L2 caching

For a L1 cache caffine is used and for L2 Redis.

This approach is nice if in a polygot environment you want to use Redis for caching.

An alternative is to use Hazelcast if in an only Java environment.

For L1 Hazelcast Near Cache is used and for L2 Hazelcast.

The tricky part is the eviction policy.