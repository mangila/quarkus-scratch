CREATE OR REPLACE FUNCTION fn_customer_cache_evict() RETURNS trigger AS
$$
DECLARE
    v_customer_id          uuid := NULL;
    v_channel              TEXT := 'customer_evict';
    v_ttl_cache_table_name TEXT := 'ttl_cache';
BEGIN
    IF TG_OP = 'DELETE' THEN
        v_customer_id := OLD.id;
    ELSE
        v_customer_id := NEW.id;
    END IF;

    --! Evict from l2 cache
    DELETE
    FROM v_ttl_cache_table_name
    WHERE key = v_customer_id::text;

    --! Notify subscribers
    PERFORM pg_notify(v_channel, v_customer_id::text);

    IF TG_OP = 'DELETE' THEN
        RETURN OLD;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;