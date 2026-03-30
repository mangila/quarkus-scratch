# Customer

Simple REST API backend with Quarkus and frontend with Astro and Vue.

## API

### L1 and L2 caching

> [!NOTE]
> "There are only two hard things in Computer Science: cache invalidation and naming things."

#### Alternative I

* L1 Caffeine
* L2 Redis

This approach is nice if in a polygot environment then use Redis for caching.

#### Alternative II

* L1 Hazelcast Near Cache
* L2 Hazelcast

An alternative is to use Hazelcast if in and only Java environment.

#### Alternative III (recommended)

* L1 Caffeine
* L2 Postgres

Using a Postgres UNLOGGED table for caching.